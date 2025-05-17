package com.project.harpyja.service.onboarding;

import com.project.harpyja.dto.request.AccountRegisterRequest;

public interface OnboardingService {

    /**
     * Processa o registro final de onboarding (atualiza user, org e cria address).
     *
     * @param orgId           ID da organização (string/UUID).
     * @param userId          ID do usuário (string/UUID).
     * @param accountRegister DTO com dados adicionais.
     */
    void processRegistration(String orgId, String userId, AccountRegisterRequest accountRegister);
}
