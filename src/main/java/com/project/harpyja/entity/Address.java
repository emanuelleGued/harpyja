package com.project.harpyja.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String country;
    private String city;
    private String street;
    private String zip;

    /**
     * Caso queira fazer um relacionamento 1-para-1 com Organization, em vez de apenas
     * manter o campo `organization_id` isolado, você pode usar @OneToOne. Veja abaixo:
     */

    @OneToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", unique = true)
    private Organization organization;

    public Address() {
    }

    public Address(UUID id, String country, String city, String street, String zip, Organization organization) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.zip = zip;
        this.organization = organization;
    }

}
