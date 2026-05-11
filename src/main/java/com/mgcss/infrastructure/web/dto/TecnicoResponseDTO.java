package com.mgcss.infrastructure.web.dto;

import com.mgcss.domain.Tecnico;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información detallada del técnico para la respuesta de la API")
public class TecnicoResponseDTO {

    @Schema(description = "Identificador único del técnico", example = "1")
    private Long id;
    
    @Schema(description = "Nombre completo del técnico", example = "Marta García")
    private String nombre;
    
    @Schema(description = "Especialidad técnica", example = "REDES")
    private String especialidad;
    
    @Schema(description = "Indica si el técnico está habilitado para recibir tareas", example = "true")
    private boolean activo;

    public TecnicoResponseDTO() {}

    public TecnicoResponseDTO(Long id, String nombre, String especialidad, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = activo;
    }

    public static TecnicoResponseDTO fromTecnico(Tecnico tecnico) {
        if (tecnico == null) return null;
        return new TecnicoResponseDTO(
            tecnico.getId(),
            tecnico.getNombre(),
            tecnico.getEspecialidad() != null ? tecnico.getEspecialidad().name() : null,
            tecnico.isActivo()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}