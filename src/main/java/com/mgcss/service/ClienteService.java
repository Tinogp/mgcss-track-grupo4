package com.mgcss.service;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    
    }

    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public boolean deleteCliente(Long id) {
        if (clienteRepository.findById(id).isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateCliente(Cliente cliente) {
        if (clienteRepository.findById(cliente.getId()).isPresent()) {
            clienteRepository.save(cliente);
            return true;
        }
        return false;
    }

    public List<Cliente> getAllCliente() {
        return clienteRepository.findAll();
    }

}