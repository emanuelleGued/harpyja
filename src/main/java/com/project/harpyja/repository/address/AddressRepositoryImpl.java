package com.project.harpyja.repository.address;

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
        if (address.getId() == null) {
            address.setId(UUID.randomUUID().toString());
        }
        entityManager.persist(address);
    }
}
