package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {

    private Long id;
    private String estado;
    private LocalDateTime fechaCreacion;

    public Solicitud() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "ABIERTA";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}