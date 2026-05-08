package com.mgcss.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mgcss.domain.repository.RegisterSolicitudRepository;
import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

@Component
public class SqlRegisterSolicitudRepository implements RegisterSolicitudRepository {

    private final SpringDataRegisterSolicitudRepository jpaRegisterSolicitudRepository;

    public SqlRegisterSolicitudRepository(SpringDataRegisterSolicitudRepository jpaRegisterSolicitudRepository) {
        this.jpaRegisterSolicitudRepository = jpaRegisterSolicitudRepository;
    }

    @Override
    public Optional<RegisterSolicitudEntity> findById(Long id) {
        return jpaRegisterSolicitudRepository.findById(id);
    }

    @Override
    public RegisterSolicitudEntity save(RegisterSolicitudEntity registerSolicitudEntity) {
        return jpaRegisterSolicitudRepository.save(registerSolicitudEntity);
    }

    @Override
    public void deleteById(Long id) {
        jpaRegisterSolicitudRepository.deleteById(id);
    }

    @Override
    public List<RegisterSolicitudEntity> findAll() {
        return jpaRegisterSolicitudRepository.findAll();
    }
}
