package com.mgcss.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@Repository
public class SqlTecnicoRepository implements TecnicoRepository {

    private final SpringDataTecnicoRepository repository;

    public SqlTecnicoRepository(SpringDataTecnicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tecnico save(Tecnico tecnico) {
        TecnicoEntity entity = toEntity(tecnico);
        TecnicoEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    private TecnicoEntity toEntity(Tecnico tecnico) {
        if (tecnico == null) return null;
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(tecnico.getId());
        entity.setNombre(tecnico.getNombre());
        if (tecnico.getEspecialidad() != null) {
            entity.setEspecialidad(tecnico.getEspecialidad().name());
        }
        entity.setActivo(tecnico.isActivo());
        return entity;
    }

    private Tecnico toDomain(TecnicoEntity entity) {
        if (entity == null) return null;
        Tecnico.Especialidad especialidad = null;
        if (entity.getEspecialidad() != null) {
            especialidad = Tecnico.Especialidad.valueOf(entity.getEspecialidad());
        }
        return new Tecnico(
            entity.getId(),
            entity.getNombre(),
            especialidad,
            entity.isActivo()
        );
    }
}
