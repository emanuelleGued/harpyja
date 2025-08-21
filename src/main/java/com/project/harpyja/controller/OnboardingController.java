package com.project.harpyja.controller;

import com.project.harpyja.dto.request.AccountRegisterRequest;
import com.project.harpyja.dto.request.IntentionAccountRegisterRequest;
import com.project.harpyja.dto.response.AccountRegisterResponse;
import com.project.harpyja.dto.response.VerifyEmailResponse;
import com.project.harpyja.email.EmailService;
import com.project.harpyja.entity.Organization;
import com.project.harpyja.entity.User;
import com.project.harpyja.entity.UserOrganization;
import com.project.harpyja.entity.UserOrganizationId;
import com.project.harpyja.exception.CustomException;
import com.project.harpyja.model.enums.OrganizationRole;
import com.project.harpyja.model.enums.OrganizationStatus;
import com.project.harpyja.service.auth.JwtUtil;
import com.project.harpyja.service.auth.PasswordUtil;
import com.project.harpyja.service.onboarding.OnboardingService;
import com.project.harpyja.service.organization.OrganizationService;
import com.project.harpyja.service.user.UserService;
import com.project.harpyja.service.user.organization.UserOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final UserService userService;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrgService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final OnboardingService onboardingService;

    @Value("${app.development-mode}")
    private String appEnv;

    public OnboardingController(UserService userService, OrganizationService organizationService,
                                UserOrganizationService userOrgService, JwtUtil jwtUtil,
                                EmailService emailService, OnboardingService onboardingService) {
        this.userService = userService;
        this.organizationService = organizationService;
        this.userOrgService = userOrgService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.onboardingService = onboardingService;
    }

    @Operation(summary = "Create a registration intention",
            description = "Starts onboarding by creating a user and organization with pending email verification",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Registration intention created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountRegisterResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request (e.g., email already exists)")
            })
    @PostMapping("/intention")
    public ResponseEntity<?> registrationIntentionOnboarding(@RequestBody IntentionAccountRegisterRequest accountRegister) {

        if (accountRegister.getEmail() == null || accountRegister.getEmail().isEmpty() ||
                accountRegister.getPassword() == null || accountRegister.getPassword().isEmpty()) {
            throw new CustomException("Email and password are required", 400);
        }

        try {
            userService.findUserByEmailServiceOnboarding(accountRegister.getEmail());

            Organization org = new Organization();
            org.setId(UUID.randomUUID().toString());
            org.setName(null);
            org.setRegistrationDate(LocalDateTime.now());
            org.setStatus(OrganizationStatus.PENDING_VERIFICATION);
            org.setBusinessSize(null);

            Organization createdOrg = organizationService.createOrganizationService(org);

            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName(null);
            user.setEmail(accountRegister.getEmail());
            user.setPassword(PasswordUtil.hashPassword(accountRegister.getPassword()));
            user.setEmailVerified(false);
            user.setTermsAgreed(false);

            User createdUser = userService.createUser(user);

            String token = jwtUtil.generateTokenToVerifyEmail(
                    accountRegister.getEmail(),
                    createdOrg.getId(),
                    createdUser.getId()
            );

            UserOrganizationId userOrgId = new UserOrganizationId();
            userOrgId.setUserId(createdUser.getId());
            userOrgId.setOrganizationId(createdOrg.getId());

            UserOrganization userOrg = new UserOrganization();
            userOrg.setId(userOrgId);
            userOrg.setRole(OrganizationRole.ADMIN);
            userOrg.setUser(createdUser);
            userOrg.setOrganization(createdOrg);

            userOrgService.createUserOrganizationService(userOrg);

            AccountRegisterResponse response = new AccountRegisterResponse(
                    createdUser.getId(),
                    createdOrg.getId(),
                    token,
                    appEnv
            );

            return ResponseEntity.status(201).body(response);

        } catch (Exception e) {
            throw new CustomException(e.getMessage(), 400);
        }
    }

    @Operation(summary = "Verify user email",
            description = "Validates email verification token and activates user account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Email verified successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = VerifyEmailResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
            })
    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {

        if (token == null || token.isEmpty()) {
            throw new CustomException("Token is required", 400);
        }

        JwtUtil.VerifiedEmailToken verifiedToken = jwtUtil.verifyEmailToken(token);

        User foundUser = userService.updateEmailVerified(verifiedToken.getEmail());
        if (foundUser == null) {
            throw new CustomException("User not found", 400);
        }

        String authToken = jwtUtil.generateAuthToken(foundUser);

        VerifyEmailResponse response = new VerifyEmailResponse(
                verifiedToken.getOrganizationId(),
                verifiedToken.getUserId(),
                foundUser,
                authToken,
                ""
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete-registration/{organization_id}/{user_id}")
    public ResponseEntity<?> completeRegistration(
            @PathVariable("organization_id") String organizationId,
            @PathVariable("user_id") String userId,
            @RequestBody AccountRegisterRequest accountRegister) {

        if (organizationId == null || organizationId.isEmpty()) {
            throw new CustomException("organization_id is required", 400);
        }
        if (userId == null || userId.isEmpty()) {
            throw new CustomException("user_id is required", 400);
        }
        if (accountRegister.getOrganizationName() == null || accountRegister.getOrganizationName().isEmpty()) {
            throw new CustomException("Organization name is required", 400);
        }
        if (accountRegister.getUserName() == null || accountRegister.getUserName().isEmpty()) {
            throw new CustomException("User name is required", 400);
        }

        onboardingService.processRegistration(organizationId, userId, accountRegister);

        return ResponseEntity.noContent().build();
    }
}
