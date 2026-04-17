package com.mgcss.domain.repository;

import java.util.Optional;

import com.mgcss.domain.Cliente;

public interface ClienteRepository {
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
}
