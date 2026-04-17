package com.mgcss.service;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.repository.ClienteRepository;
import java.util.Optional;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    
    }

}
