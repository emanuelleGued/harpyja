package com.project.harpyja.repository;

import com.project.harpyja.entity.Address;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createAddressRepository(Address address) {
        // Se já não tiver ID, gerar
        if (address.getId() == null) {
            address.setId(UUID.fromString(UUID.randomUUID().toString()));
        }
        entityManager.persist(address);
    }
}
