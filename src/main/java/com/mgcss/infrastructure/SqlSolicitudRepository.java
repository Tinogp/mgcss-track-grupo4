package com.mgcss.infrastructure;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.Tecnico;

import org.springframework.stereotype.Component;

import java.util.Optional;

import com.mgcss.infrastructure.persistence.SolicitudEntity;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@Component
public class SqlSolicitudRepository implements SolicitudRepository {

    private final SpringDataSolicitudRepository jpaSolicitudRepo;

    public SqlSolicitudRepository(SpringDataSolicitudRepository jpaSolicitudRepo) {
        this.jpaSolicitudRepo = jpaSolicitudRepo;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        // Mapeo: Dominio -> Entidad
        SolicitudEntity entity = toEntity(solicitud);
        SolicitudEntity saved = jpaSolicitudRepo.save(entity);
        // Mapeo: Entidad -> Dominio
        return toDomain(saved);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return jpaSolicitudRepo.findById(id).map(this::toDomain);
    }

    // --- MAPPERS PRIVADOS ---

    private SolicitudEntity toEntity(Solicitud dominio) {
        if (dominio == null) return null;
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(dominio.getId());
        entity.setDescripcion(dominio.getDescripcion());
        entity.setFechaCreacion(dominio.getFechaCreacion());
        entity.setEstadoActual(dominio.getEstado().name());
        entity.setFechaCierre(dominio.getFechaCierre());

        if (dominio.getTecnicoAsignado() != null) {
            TecnicoEntity tEntity = new TecnicoEntity();
            tEntity.setId(dominio.getTecnicoAsignado().getId());
            tEntity.setNombre(dominio.getTecnicoAsignado().getNombre());
            tEntity.setEspecialidad(dominio.getTecnicoAsignado().getEspecialidad().name());
            tEntity.setActivo(dominio.getTecnicoAsignado().isActivo());
            entity.setTecnicoAsignado(tEntity);
        }
        return entity;
    }

    private Solicitud toDomain(SolicitudEntity entity) {
        if (entity == null) return null;
        Tecnico tecnico = null;
        if (entity.getTecnicoAsignado() != null) {
            tecnico = new Tecnico(
                entity.getTecnicoAsignado().getId(),
                entity.getTecnicoAsignado().getNombre(),
                Tecnico.Especialidad.valueOf(entity.getTecnicoAsignado().getEspecialidad()),
                entity.getTecnicoAsignado().isActivo()
            );
        }
        return new Solicitud(
            entity.getId(),
            entity.getDescripcion(),
            entity.getFechaCreacion(),
            Solicitud.Estado.valueOf(entity.getEstadoActual()),
            tecnico,
            entity.getFechaCierre()
        );
    }
}