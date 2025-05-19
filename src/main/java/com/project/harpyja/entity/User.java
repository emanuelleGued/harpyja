package com.project.harpyja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    private String name;

    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "terms_agreed")
    private boolean termsAgreed;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String id, String name, String email, String password,
                boolean emailVerified, boolean termsAgreed) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailVerified = emailVerified;
        this.termsAgreed = termsAgreed;
    }
}