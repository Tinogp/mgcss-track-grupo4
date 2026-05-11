package com.mgcss.infrastructure.web.dto;

import com.mgcss.domain.Tecnico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para registrar un nuevo técnico")
public class TecnicoRequestDTO {
    
    @Schema(description = "Nombre completo del técnico", example = "Marta García")
    private String nombre;
    
    @Schema(description = "Área de especialización técnica", example = "REDES")
    private Tecnico.Especialidad especialidad;

    public TecnicoRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Tecnico.Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Tecnico.Especialidad especialidad) { this.especialidad = especialidad; }
}