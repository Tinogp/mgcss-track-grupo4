package com.mgcss.infrastructure.web.mapper;

import org.springframework.stereotype.Component;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.web.dto.TecnicoRequestDTO; // Asumiendo que crearás uno similar al de Cliente
import com.mgcss.infrastructure.web.dto.TecnicoResponseDTO;

@Component
public class TecnicoMapper {

    public Tecnico toDomain(TecnicoRequestDTO dto) {
        if (dto == null) return null;
        return new Tecnico(dto.getNombre(), dto.getEspecialidad());
    }

    public TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        return TecnicoResponseDTO.fromTecnico(tecnico);
    }
}