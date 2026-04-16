package com.mgcss.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgcss.infrastructure.persistence.TecnicoEntity;

@Repository
public interface SpringDataTecnicoRepository extends JpaRepository<TecnicoEntity, Long> {
}