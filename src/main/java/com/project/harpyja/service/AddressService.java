package com.project.harpyja.service;


import com.project.harpyja.model.Address;

public interface AddressService {
    /**
     * Cria um novo endereço no sistema.
     * @param address objeto de endereço a ser persistido.
     */
    void createAddressService(Address address);
}
