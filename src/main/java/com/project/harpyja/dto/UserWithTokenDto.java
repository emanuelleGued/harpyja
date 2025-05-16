package com.project.harpyja.dto;


import com.project.harpyja.entity.User;

public class UserWithTokenDto {
    private User user;
    private String token;
    private String projectKey; // se você quiser manter esse campo

    public UserWithTokenDto() {
    }

    public UserWithTokenDto(User user, String token, String projectKey) {
        this.user = user;
        this.token = token;
        this.projectKey = projectKey;
    }

    // Getters e Setters
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
