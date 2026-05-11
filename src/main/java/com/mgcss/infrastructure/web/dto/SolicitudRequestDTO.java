package com.mgcss.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para crear una nueva solicitud de mantenimiento")
public class SolicitudRequestDTO {
    
    @Schema(description = "Descripción detallada del fallo o servicio", example = "El servidor no arranca tras el corte de luz")
    private String descripcion;
    
    @Schema(description = "ID del cliente propietario de la solicitud", example = "10")
    private Long clienteId;

    public SolicitudRequestDTO() {}

    public SolicitudRequestDTO(String descripcion, Long clienteId) {
        this.descripcion = descripcion;
        this.clienteId = clienteId;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}