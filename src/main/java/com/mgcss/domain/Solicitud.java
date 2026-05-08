package com.mgcss.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

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
    private final List<RegisterSolicitudEntity> historialEstado = new ArrayList<>();

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

    public List<RegisterSolicitudEntity> getHistorialEstado() {
        return Collections.unmodifiableList(historialEstado);
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
        return iniciarProcesoConRegistro() != null;
    }

    public RegisterSolicitudEntity iniciarProcesoConRegistro() {
        if (Estado.ABIERTA.equals(this.estadoActual) && this.tecnicoAsignado != null) {
            Estado anterior = this.estadoActual;
            this.estadoActual = Estado.EN_PROCESO;
            RegisterSolicitudEntity registro = crearRegistro(anterior, this.estadoActual);
            historialEstado.add(registro);
            return registro;
        }
        return null;
    }

    public boolean cerrar() {
        return cerrarConRegistro() != null;
    }

    public RegisterSolicitudEntity cerrarConRegistro() {
        if (Estado.EN_PROCESO.equals(this.estadoActual)) {
            Estado anterior = this.estadoActual;
            this.estadoActual = Estado.CERRADA;
            this.fechaCierre = LocalDateTime.now();
            RegisterSolicitudEntity registro = crearRegistro(anterior, this.estadoActual);
            historialEstado.add(registro);
            return registro;
        }
        return null;
    }

    public boolean reabrir() {
        return reabrirConRegistro() != null;
    }

    public RegisterSolicitudEntity reabrirConRegistro() {
        if (Estado.CERRADA.equals(this.estadoActual)) {
            Estado anterior = this.estadoActual;
            this.estadoActual = Estado.ABIERTA;
            this.fechaCierre = null;
            RegisterSolicitudEntity registro = crearRegistro(anterior, this.estadoActual);
            historialEstado.add(registro);
            return registro;
        }
        return null;
    }

    private RegisterSolicitudEntity crearRegistro(Estado anterior, Estado actual) {
        return new RegisterSolicitudEntity(this.id, anterior, actual);
    }
}
