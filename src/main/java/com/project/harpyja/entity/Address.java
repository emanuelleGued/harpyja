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
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    private String country;
    private String city;
    private String street;
    private String zip;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Relacionamento 1-para-1 com Organization
     */
    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", unique = true)
    private Organization organization;

    public Address() {
    }

    public Address(String id, String country, String city, String street, String zip, Organization organization) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.organization = organization;
    }
}