package com.project.harpyja.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class AccountRegisterResponse {

    // Setters
    private String userId;
    private String organizationId;

    @JsonIgnore
    private String token;

    private String appEnv;

    public AccountRegisterResponse() {
    }

    public AccountRegisterResponse(String userId, String organizationId) {
        this.userId = userId;
        this.organizationId = organizationId;
    }

    public AccountRegisterResponse(String userId, String organizationId, String token) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.token = token;
    }

    public AccountRegisterResponse(String userId, String organizationId, String token, String appEnv) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.token = token;
        this.appEnv = appEnv;
    }

    // Getters normais
    public String getUserId() {
        return userId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    // Token só aparece se em modo dev
    @JsonProperty("token")
    public String getTokenIfDev() {
        if ("dev".equalsIgnoreCase(appEnv)) {
            return token;
        }
        return null;
    }
}
