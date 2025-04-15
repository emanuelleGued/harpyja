package com.project.harpyja.dto.response;

public class AccountRegisterResponse {
    private String userId;
    private String organizationId;

    // construtor, getters e setters
    public AccountRegisterResponse(String userId, String organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
    }

    // getters e setters
}