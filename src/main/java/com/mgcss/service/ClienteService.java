package com.mgcss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    
    }

    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    /**
     * Crear un nuevo cliente con los datos proporcionados.
     *
     * @param nombre Nombre del cliente
     * @param email Email del cliente
     * @param tipoCliente Tipo de cliente (STANDARD o PREMIUM)
     * @return Cliente creado y persistido
     */
    public Cliente crearCliente(String nombre, String email, Cliente.TipoCliente tipoCliente) {
        Cliente nuevoCliente = new Cliente(nombre, email, tipoCliente);
        return clienteRepository.save(nuevoCliente);
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