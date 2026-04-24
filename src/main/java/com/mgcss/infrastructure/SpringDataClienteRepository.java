package com.mgcss.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgcss.infrastructure.persistence.ClienteEntity;

@Repository
public interface SpringDataClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
