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
import com.project.harpyja.model.enums.OrganizationRole;
import com.project.harpyja.model.enums.OrganizationStatus;
import com.project.harpyja.service.auth.JwtUtil;
import com.project.harpyja.service.auth.PasswordUtil;
import com.project.harpyja.service.onboarding.OnboardingService;
import com.project.harpyja.service.organization.OrganizationService;
import com.project.harpyja.service.user.UserService;
import com.project.harpyja.service.user.organization.UserOrganizationService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserOrganizationService userOrgService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.development-mode}")
    private String appEnv;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OnboardingService onboardingService;

    /**
     * POST /api/onboarding/intention
     * Cria intenção de registro: cria Organization (PendingVerification), cria User (email/password), etc.
     */
    @PostMapping(value = "/intention")
    public ResponseEntity<?> registrationIntentionOnboarding(
            @RequestBody IntentionAccountRegisterRequest accountRegister) {
        try {
            userService.findUserByEmailServiceOnboarding(accountRegister.getEmail());

            // 2. Cria Organization com status = PENDING_VERIFICATION
            Organization org = new Organization();
            org.setId(UUID.randomUUID().toString());
            org.setName(null);
            org.setRegistrationDate(LocalDateTime.now());
            org.setStatus(OrganizationStatus.PENDING_VERIFICATION);
            org.setBusinessSize(null);

            Organization createdOrg = organizationService.createOrganizationService(org);

            // 3. Cria User com email/password, emailVerified=false
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName(null); // perguntar a hugo
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

            //emailService.sendVerificationEmail(createdUser.getEmail(), token);

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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable String token) {
        try {
            if (token == null || token.isEmpty()) {
                throw new BadRequestException("Token is required");
            }
            JwtUtil.VerifiedEmailToken verifiedToken = jwtUtil.verifyEmailToken(token);

            String email = verifiedToken.getEmail();
            String organizationId = verifiedToken.getOrganizationId();
            String userId = verifiedToken.getUserId();

            if (organizationId == null || userId == null) {
                throw new BadRequestException("Failed to extract claims from token");
            }

            User foundUser = userService.updateEmailVerified(email);

            if (foundUser == null) {
                throw new BadRequestException("User not found");
            }

            String authToken = jwtUtil.generateAuthToken(foundUser);

            VerifyEmailResponse response = new VerifyEmailResponse(
                    organizationId,
                    userId,
                    foundUser,
                    authToken,
                    "" // projectKey pode ser adicionado se necessário
            );

            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }

    @PostMapping("/complete-registration/{organization_id}/{user_id}")
    public ResponseEntity<?> completeRegistration(
            @PathVariable("organization_id") String organizationId,
            @PathVariable("user_id") String userId,
            @RequestBody AccountRegisterRequest accountRegister) {

        try {
            if (organizationId == null || organizationId.isEmpty()) {
                return ResponseEntity.badRequest().body("organization_id is required");
            }

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body("user_id is required");
            }

            if (accountRegister.getOrganizationName() == null || accountRegister.getOrganizationName().isEmpty()) {
                return ResponseEntity.badRequest().body("Organization name is required");
            }
            if (accountRegister.getUserName() == null || accountRegister.getUserName().isEmpty()) {
                return ResponseEntity.badRequest().body("User name is required");
            }

            onboardingService.processRegistration(organizationId, userId, accountRegister);

            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error completing registration: " + e.getMessage());
        }
    }
}
