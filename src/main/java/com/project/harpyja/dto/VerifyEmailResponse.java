package com.project.harpyja.dto;


import com.project.harpyja.model.User;

public class VerifyEmailResponse {
    private String organizationId;
    private String userId;
    private User user;
    private String token;
    private String projectKey;

    public VerifyEmailResponse() {
    }

    public VerifyEmailResponse(String organizationId, String userId, User user, String token, String projectKey) {
        this.organizationId = organizationId;
        this.userId = userId;
        this.user = user;
        this.token = token;
        this.projectKey = projectKey;
    }

    // Getters e Setters
    public String getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getProjectKey() {
        return projectKey;
    }
    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
}
