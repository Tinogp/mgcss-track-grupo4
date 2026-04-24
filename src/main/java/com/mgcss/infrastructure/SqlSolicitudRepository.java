package com.mgcss.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.infrastructure.persistence.ClienteEntity;
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
        entity.setEstadoActual(dominio.getEstadoActual().name());
        entity.setFechaCierre(dominio.getFechaCierre());

        // 1. Mapear el Técnico (como ya teníamos)
        if (dominio.getTecnicoAsignado() != null) {
            TecnicoEntity tEntity = new TecnicoEntity();
            tEntity.setId(dominio.getTecnicoAsignado().getId());
            tEntity.setNombre(dominio.getTecnicoAsignado().getNombre());
            tEntity.setEspecialidad(dominio.getTecnicoAsignado().getEspecialidad().name());
            tEntity.setActivo(dominio.getTecnicoAsignado().isActivo());
            entity.setTecnicoAsignado(tEntity);
        }

        // 2. NUEVO: Mapear el Cliente
        if (dominio.getCliente() != null) {
            ClienteEntity cEntity = new ClienteEntity();
            cEntity.setId(dominio.getCliente().getId());
            cEntity.setNombre(dominio.getCliente().getNombre());
            cEntity.setEmail(dominio.getCliente().getEmail());
            cEntity.setTipoCliente(dominio.getCliente().getTipoCliente().name()); // Enum a String
            entity.setCliente(cEntity);
        }

        return entity;
    }

    private Solicitud toDomain(SolicitudEntity entity) {
        if (entity == null) return null;
        
        // 1. Reconstruir Técnico
        Tecnico tecnico = null;
        if (entity.getTecnicoAsignado() != null) {
            tecnico = new Tecnico(
                entity.getTecnicoAsignado().getId(),
                entity.getTecnicoAsignado().getNombre(),
                Tecnico.Especialidad.valueOf(entity.getTecnicoAsignado().getEspecialidad()),
                entity.getTecnicoAsignado().isActivo()
            );
        }

        // 2. NUEVO: Reconstruir Cliente
        Cliente cliente = null;
        if (entity.getCliente() != null) {
            cliente = new Cliente(
                entity.getCliente().getId(),
                entity.getCliente().getNombre(),
                entity.getCliente().getEmail(),
                Cliente.TipoCliente.valueOf(entity.getCliente().getTipoCliente()) // String a Enum
            );
        }

        // 3. Devolver la Solicitud completa usando el constructor actualizado
        return new Solicitud(
            entity.getId(),
            entity.getDescripcion(),
            cliente, // Pasamos el cliente aquí
            entity.getFechaCreacion(),
            Solicitud.Estado.valueOf(entity.getEstadoActual()),
            tecnico,
            entity.getFechaCierre()
        );
    }
}