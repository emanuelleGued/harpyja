package com.project.harpyja.service.address;

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
        address.setId(UUID.randomUUID().toString());

        addressRepository.createAddressRepository(address);
    }
}
