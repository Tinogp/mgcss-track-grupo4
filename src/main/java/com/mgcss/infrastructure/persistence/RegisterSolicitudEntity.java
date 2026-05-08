package com.mgcss.infrastructure.persistence;

import java.time.LocalDateTime;

import com.mgcss.domain.Solicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "register_solicitud")
public class RegisterSolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private Solicitud.Estado estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual")
    private Solicitud.Estado estadoActual;

    public RegisterSolicitudEntity(){
        // Constructor vacío obligatorio para JPA
    }

    public RegisterSolicitudEntity(Long solicitudId, Solicitud.Estado estadoAnterior, Solicitud.Estado estadoActual) {
        this.solicitudId = solicitudId;
        this.fechaHora = LocalDateTime.now();
        this.estadoAnterior = estadoAnterior;
        this.estadoActual = estadoActual;
    }

    public Long getId() {
        return id;
    }

    public Long getSolicitudId() {
        return solicitudId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Solicitud.Estado getEstadoAnterior() {
        return estadoAnterior;
    }

    public Solicitud.Estado getEstadoActual() {
        return estadoActual;
    }
}
