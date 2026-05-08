package com.mgcss.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

public interface SpringDataRegisterSolicitudRepository extends JpaRepository<RegisterSolicitudEntity, Long> {
}
