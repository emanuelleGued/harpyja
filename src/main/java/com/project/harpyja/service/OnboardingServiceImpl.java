package com.project.harpyja.service;

import com.project.harpyja.dto.request.AccountRegisterRequest;
import com.project.harpyja.model.Address;
import com.project.harpyja.model.Organization;
import com.project.harpyja.model.User;
import com.project.harpyja.model.enums.OrganizationStatus;
import com.project.harpyja.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    @Autowired
    private UserService userService;

    // Troque de "OrganizationServiceImpl" para a interface "OrganizationService"
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private AddressService addressService;

    @Override
    @Transactional
    public void processRegistration(String orgId, String userId, AccountRegisterRequest accountRegister) {
        // 1. Atualiza o usuário
        User updatesForUser = new User();
        updatesForUser.setName(accountRegister.getUserName());
        userService.updateUserService(userId, updatesForUser);

        // 2. Atualiza a organização
        Organization orgUpdates = new Organization();
        orgUpdates.setName(accountRegister.getOrganizationName());
        orgUpdates.setStatus(OrganizationStatus.COMPLETE);
        orgUpdates.setBusinessSize(accountRegister.getBusinessSize());
        organizationService.updateOrganizationService(orgId, orgUpdates);

        // 3. Carrega a entidade Organization atualizada
        //    (agora que existe o método findById no service)
        Organization fullOrg = organizationService.findById(orgId);

        // 4. Cria o address
        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setCountry(accountRegister.getCountry());
        address.setCity(accountRegister.getCity());
        address.setStreet(accountRegister.getStreet());
        address.setZip(accountRegister.getZip());
        address.setOrganization(fullOrg);

        addressService.createAddressService(address);
    }
}
