package com.mgcss.domain.repository;

import java.util.Optional;
import java.util.List;

import com.mgcss.domain.Cliente;

public interface ClienteRepository {
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
    void deleteById(Long id);
    List<Cliente> findAll();
}
