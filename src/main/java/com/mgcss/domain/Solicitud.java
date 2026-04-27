package com.mgcss.domain;

import java.time.LocalDateTime;

public class Solicitud {

    public enum Estado {
        ABIERTA, CERRADA, EN_PROCESO
    }

    private Long id;
    private Cliente cliente;
    private String descripcion;
    private final LocalDateTime fechaCreacion;
    private Estado estadoActual;
    private Tecnico tecnicoAsignado;
    private LocalDateTime fechaCierre;

    public Solicitud(Cliente cliente){
        this.fechaCreacion = LocalDateTime.now();
        this.fechaCierre = null;
        this.estadoActual = Estado.ABIERTA;
        this.cliente = cliente;
    }

    public Solicitud(Long id, String descripcion, Cliente cliente,LocalDateTime fechaCreacion, Estado estadoActual, Tecnico tecnicoAsignado, LocalDateTime fechaCierre) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estadoActual = estadoActual;
        this.tecnicoAsignado = tecnicoAsignado;
        this.fechaCierre = fechaCierre;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean setDescripcion(String descripcion) {
        if (estadoActual != Estado.CERRADA) {
            this.descripcion = descripcion;
            return true;
        }
        return false;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public Tecnico getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public boolean asignarTecnico(Tecnico tecnicoAsignado) {
        if ((Estado.ABIERTA.equals(this.estadoActual) || Estado.EN_PROCESO.equals(this.estadoActual)) && tecnicoAsignado.isActivo()) {
            this.tecnicoAsignado = tecnicoAsignado;
            return true;
        } else {
            return false;
        }
    }

    public boolean iniciarProceso() {
        if (Estado.ABIERTA.equals(this.estadoActual) && this.tecnicoAsignado != null) {
            this.estadoActual = Estado.EN_PROCESO;
            return true;
        }
        return false;
    }

    public boolean cerrar() {
        if (Estado.EN_PROCESO.equals(this.estadoActual)) {
            this.estadoActual = Estado.CERRADA;
            this.fechaCierre = LocalDateTime.now();
            return true;
        }
        return false;
    }

}
