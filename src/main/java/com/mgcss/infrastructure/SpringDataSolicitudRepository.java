package com.mgcss.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgcss.infrastructure.persistence.SolicitudEntity;

@Repository
public interface SpringDataSolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
}