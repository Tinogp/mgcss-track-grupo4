package com.mgcss.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la asignación de un técnico a una solicitud existente")
public class AsignarTecnicoDTO {
    
    @Schema(description = "Identificador del técnico que se desea asignar", example = "1")
    private Long tecnicoId;

    public AsignarTecnicoDTO() {}

    public AsignarTecnicoDTO(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }
}