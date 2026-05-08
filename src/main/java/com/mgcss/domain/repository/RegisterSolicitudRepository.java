package com.mgcss.domain.repository;

import java.util.List;
import java.util.Optional;

import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;


public interface RegisterSolicitudRepository {

    Optional<RegisterSolicitudEntity> findById(Long id);

    RegisterSolicitudEntity save(RegisterSolicitudEntity registerSolicitudEntity);

    void deleteById(Long id);

    List<RegisterSolicitudEntity> findAll();
}
