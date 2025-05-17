package com.project.harpyja.service;

import com.project.harpyja.entity.Address;
import com.project.harpyja.repository.address.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void createAddressService(Address address) {
        // Gera novo ID (caso não esteja definido)
        address.setId(UUID.fromString(UUID.randomUUID().toString()));

        // Chama repositório para persistir
        addressRepository.createAddressRepository(address);
    }
}
