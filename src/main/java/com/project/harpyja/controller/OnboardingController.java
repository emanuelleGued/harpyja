package com.project.harpyja.controller;

import com.project.harpyja.auth.Auth;
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
import com.project.harpyja.service.organization.OrganizationService;
import com.project.harpyja.service.user.UserService;
import com.project.harpyja.service.user.organization.UserOrganizationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    private Auth authService;

    @Value("${app.development-mode}")
    private String appEnv;

    @Autowired
    private EmailService emailService;

    @Value("${jwt.secret}")
    private String jwtSecret;

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
            org.setId(UUID.randomUUID());
            org.setName(null);
            org.setRegistrationDate(LocalDateTime.now());
            org.setStatus(OrganizationStatus.PENDING_VERIFICATION);
            org.setBusinessSize(null);

            Organization createdOrg = organizationService.createOrganizationService(org);

            // 3. Cria User com email/password, emailVerified=false
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName(null); // perguntar a hugo
            user.setEmail(accountRegister.getEmail());
            user.setPassword(accountRegister.getPassword()); // se tiver hash, coloque
            user.setEmailVerified(false);
            user.setTermsAgreed(false);

            User createdUser = userService.createUser(user);

            String token = authService.generateTokenToVerifyEmail(
                    accountRegister.getEmail(),
                    createdOrg.getId().toString(),
                    createdUser.getId().toString()
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
                    createdUser.getId().toString(),
                    createdOrg.getId().toString(),
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
            // 1. Verifica se o token foi fornecido
            if (token == null || token.isEmpty()) {
                throw new BadRequestException("Token is required");
            }

            // 2. Parse e validação do token
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            // 3. Extrai claims do token
            String organizationId = claims.get("organizationId", String.class);
            String userId = claims.get("userId", String.class);
            String email = claims.get("email", String.class);

            if (organizationId == null || userId == null) {
                throw new BadRequestException("Failed to extract claims from token");
            }

            User foundUser = userService.updateEmailVerified(email);

            if (foundUser == null) {
                throw new BadRequestException("User not found");
            }

            // 5. Gera novo token JWT para autenticação
            String authToken = authService.generateAuthToken(foundUser);

            // 6. Retorna resposta
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
}
