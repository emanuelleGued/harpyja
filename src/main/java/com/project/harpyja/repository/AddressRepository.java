package com.project.harpyja.repository;


import com.project.harpyja.model.Address;

public interface AddressRepository {

    /**
     * Cria um novo address no banco.
     */
    void createAddressRepository(Address address);
}
