package com.project.harpyja.controller;

import com.project.harpyja.dto.request.AccountRegisterRequest;
import com.project.harpyja.dto.request.IntentionAccountRegisterRequest;
import com.project.harpyja.dto.response.AccountRegisterResponse;
import com.project.harpyja.model.Organization;
import com.project.harpyja.model.User;
import com.project.harpyja.model.UserOrganization;
import com.project.harpyja.model.UserOrganizationId;
import com.project.harpyja.model.enums.OrganizationRole;
import com.project.harpyja.model.enums.OrganizationStatus;
import com.project.harpyja.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AddressService addressService;

    @Autowired
    private OnboardingService onboardingService;


    /**
     * POST /api/onboarding/intention
     * Cria intenção de registro: cria Organization (PendingVerification), cria User (email/password), etc.
     */
    @PostMapping("/intention")
    public ResponseEntity<?> registrationIntentionOnboarding(
            @RequestBody IntentionAccountRegisterRequest accountRegister)
    {
        try {
            // 1. Verifica se e-mail já existe (OnboardingService ou UserService)
            userService.findUserByEmailServiceOnboarding(accountRegister.getEmail());
            // Se não lançar exceção, significa que não existe -> pode prosseguir

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
            user.setName(null);
            user.setEmail(accountRegister.getEmail());
            user.setPassword(accountRegister.getPassword()); // se tiver hash, coloque
            user.setEmailVerified(false);
            user.setTermsAgreed(false);

            User createdUser = userService.createUser(user);

            UserOrganizationId userOrgId = new UserOrganizationId();
            userOrgId.setUserId(createdUser.getId());
            userOrgId.setOrganizationId(createdOrg.getId());

            UserOrganization userOrg = new UserOrganization();
            userOrg.setId(userOrgId);
            userOrg.setRole(OrganizationRole.ADMIN);
            userOrg.setUser(createdUser);
            userOrg.setOrganization(createdOrg);

            userOrgService.createUserOrganizationService(userOrg);


            // 6. Retorna AccountRegisterResponse com userId e orgId
            AccountRegisterResponse response = new AccountRegisterResponse(
                    createdUser.getId().toString(),
                    createdOrg.getId().toString()
            );
            return ResponseEntity.status(201).body(response);

        } catch (Exception e) {
            // Lida com erros e devolve algo como 400 Bad Request
            // ou 409 Conflict se e-mail já existe, etc.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * POST /api/onboarding/register/{organization_id}/{user_id}
     * Finaliza o onboarding (ProcessRegistration).
     */
    @PostMapping("/register/{organization_id}/{user_id}")
    public ResponseEntity<?> registerController(
            @PathVariable("organization_id") String organizationId,
            @PathVariable("user_id") String userId,
            @RequestBody AccountRegisterRequest accountRegister
    ) {
        // 1. Valida se orgId e userId foram enviados
        if (organizationId == null || organizationId.isEmpty()) {
            return ResponseEntity.badRequest().body("organization_id is required");
        }
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().body("user_id is required");
        }

        try {
            // 2. Chama OnboardingService.processRegistration
            onboardingService.processRegistration(organizationId, userId, accountRegister);

            // 3. Se deu certo, retorna 204 No Content
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
