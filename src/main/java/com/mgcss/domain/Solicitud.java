package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {

    public enum Estado {
        ABIERTA, CERRADA, EN_PROCESO
    };

    private Long id;
    private String descripcion;
    private final LocalDateTime fechaCreacion;
    private Estado estado_actual;
    private Tecnico tecnicoAsignado;
    private LocalDateTime fechaCierre;

    public Solicitud() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCierre = null;
        this.estado_actual = Estado.ABIERTA;
    }

    public Long getId() {
        return id;
    }

    public Estado getEstado() {
        return estado_actual;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean setDescripcion(String descripcion) {
        if (estado_actual != Estado.CERRADA) {
            this.descripcion = descripcion;
            return true;
        }
        return false;
    }

    public Estado getEstado_actual() {
        return estado_actual;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public Tecnico getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public boolean asignarTecnico(Tecnico tecnicoAsignado) {
        if ((Estado.ABIERTA.equals(this.estado_actual) || Estado.EN_PROCESO.equals(this.estado_actual)) && tecnicoAsignado.isActivo()) {
            this.tecnicoAsignado = tecnicoAsignado;
            return true;
        } else {
            return false;
        }
    }

    public boolean iniciarProceso() {
        if (Estado.ABIERTA.equals(this.estado_actual) && this.tecnicoAsignado != null) {
            this.estado_actual = Estado.EN_PROCESO;
            return true;
        }
        return false;
    }

    public boolean cerrar() {
        if (Estado.EN_PROCESO.equals(this.estado_actual)) {
            this.estado_actual = Estado.CERRADA;
            this.fechaCierre = LocalDateTime.now();
            return true;
        }
        return false;
    }

}
