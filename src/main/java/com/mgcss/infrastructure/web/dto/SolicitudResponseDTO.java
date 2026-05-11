package com.mgcss.infrastructure.web.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado completo de una solicitud de servicio")
public class SolicitudResponseDTO {

    @Schema(description = "ID de la solicitud", example = "100")
    private Long id;
    
    @Schema(description = "Descripción de la incidencia", example = "El servidor no arranca")
    private String descripcion;
    
    @Schema(description = "Estado actual del proceso", example = "EN_PROCESO")
    private String estadoActual;
    
    @Schema(description = "Fecha de apertura de la solicitud")
    private LocalDateTime fechaCreacion;
    
    @Schema(description = "Fecha de finalización (si aplica)")
    private LocalDateTime fechaCierre;
    
    @Schema(description = "ID del cliente solicitante", example = "10")
    private Long clienteId;
    
    @Schema(description = "Nombre del cliente", example = "Talleres Pérez")
    private String clienteNombre;
    
    @Schema(description = "ID del técnico asignado", example = "1")
    private Long tecnicoId;
    
    @Schema(description = "Nombre del técnico encargado", example = "Marta García")
    private String tecnicoNombre;

    public SolicitudResponseDTO() {}

    // Getters y Setters necesarios para el mapper
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public Long getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(Long tecnicoId) { this.tecnicoId = tecnicoId; }
    public String getTecnicoNombre() { return tecnicoNombre; }
    public void setTecnicoNombre(String tecnicoNombre) { this.tecnicoNombre = tecnicoNombre; }
}