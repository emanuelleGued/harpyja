package com.project.harpyja.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String name;

    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "terms_agreed")
    private boolean termsAgreed;

    /**
     * Se quiser que o relacionamento com UserOrganization / UserProject seja bidirecional,
     * pode adicionar:
     *
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     * private List<UserOrganization> organizations;
     * @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     * private List<UserProject> projects;
     */

    public User() {
    }

    public User(UUID id, String name, String email, String password, boolean emailVerified, boolean termsAgreed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.termsAgreed = termsAgreed;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isTermsAgreed() {
        return termsAgreed;
    }

    public void setTermsAgreed(boolean termsAgreed) {
        this.termsAgreed = termsAgreed;
    }

    public boolean comparePasswords(String rawPassword) {
        return this.password != null && this.password.equals(rawPassword);
    }

    public void sanitizePassword() {
        this.password = null;
    }
}
