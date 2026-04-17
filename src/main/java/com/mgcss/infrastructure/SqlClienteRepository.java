package com.mgcss.infrastructure;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.repository.ClienteRepository;

import org.springframework.stereotype.Component;

import java.util.Optional;

import com.mgcss.infrastructure.persistence.ClienteEntity;

@Component
public class SqlClienteRepository implements ClienteRepository {

    private final SpringDataClienteRepository jpaClienteRepo;

    public SqlClienteRepository(SpringDataClienteRepository jpaClienteRepo) {
        this.jpaClienteRepo = jpaClienteRepo;
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Mapeo: Dominio -> Entidad
        ClienteEntity entity = toEntity(cliente);
        ClienteEntity saved = jpaClienteRepo.save(entity);
        // Mapeo: Entidad -> Dominio
        return toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaClienteRepo.findById(id).map(this::toDomain);
    }

    // --- MAPPERS PRIVADOS ---

    private ClienteEntity toEntity(Cliente dominio) {
        if (dominio == null) return null;
        
        ClienteEntity entity = new ClienteEntity();
        entity.setId(dominio.getId());
        entity.setNombre(dominio.getNombre());
        entity.setEmail(dominio.getEmail());
        entity.setTipoCliente(dominio.getTipoCliente().name()); // Enum a String

        return entity;
    }

    private Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        
        return new Cliente(
            entity.getId(),
            entity.getNombre(),
            entity.getEmail(),
            Cliente.TipoCliente.valueOf(entity.getTipoCliente()) // String a Enum
        );
    }
}
