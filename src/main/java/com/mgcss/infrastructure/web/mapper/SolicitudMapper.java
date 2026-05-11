package com.mgcss.infrastructure.web.mapper;

import com.mgcss.domain.Solicitud;
import com.mgcss.infrastructure.web.dto.SolicitudResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {

    public SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        if (solicitud == null) {
            return null;
        }

        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setEstadoActual(solicitud.getEstadoActual() != null ? solicitud.getEstadoActual().name() : null);
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setFechaCierre(solicitud.getFechaCierre());

        if (solicitud.getCliente() != null) {
            dto.setClienteId(solicitud.getCliente().getId());
            dto.setClienteNombre(solicitud.getCliente().getNombre());
        }

        if (solicitud.getTecnicoAsignado() != null) {
            dto.setTecnicoId(solicitud.getTecnicoAsignado().getId());
            dto.setTecnicoNombre(solicitud.getTecnicoAsignado().getNombre());
        }

        return dto;
    }
}
