package com.project.harpyja.service;


import com.project.harpyja.entity.Address;

public interface AddressService {
    /**
     * Cria um novo endereço no sistema.
     *
     * @param address objeto de endereço a ser persistido.
     */
    void createAddressService(Address address);
}
