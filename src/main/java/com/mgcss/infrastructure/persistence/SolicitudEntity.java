package com.mgcss.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente; // Usamos ClienteEntity, no el Cliente del dominio
    
    private LocalDateTime fechaCreacion;

    // Guardamos el Enum como un String en la BD para que sea legible (ej. "ABIERTA")
    private String estadoActual;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnicoAsignado; // Usamos TecnicoEntity, no el Tecnico del dominio

    private LocalDateTime fechaCierre;

    // Constructor vacío obligatorio para JPA
    public SolicitudEntity() {
    }

    // --- Solo Getters y Setters a partir de aquí ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public TecnicoEntity getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(TecnicoEntity tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
}
