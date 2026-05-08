package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.RegisterSolicitudRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final RegisterSolicitudRepository registerSolicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
            TecnicoRepository tecnicoRepository,
            RegisterSolicitudRepository registerSolicitudRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.registerSolicitudRepository = registerSolicitudRepository;
    }

    public boolean asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe"));

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("El técnico con ID " + tecnicoId + " no existe"));

        boolean asignado = solicitud.asignarTecnico(tecnico);

        if (asignado) {
            solicitudRepository.save(solicitud);
            return true;
        }

        return false;
    }

    public boolean iniciarProceso(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe"));

        RegisterSolicitudEntity registro = solicitud.iniciarProcesoConRegistro();
        if (registro != null) {
            registerSolicitudRepository.save(registro);
            solicitudRepository.save(solicitud);
            return true;
        }
        return false;
    }

    public boolean cerrarSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe"));

        RegisterSolicitudEntity registro = solicitud.cerrarConRegistro();
        if (registro != null) {
            registerSolicitudRepository.save(registro);
            solicitudRepository.save(solicitud);
            return true;
        }
        return false;
    }

    public boolean reabrirSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe"));

        RegisterSolicitudEntity registro = solicitud.reabrirConRegistro();
        if (registro != null) {
            registerSolicitudRepository.save(registro);
            solicitudRepository.save(solicitud);
            return true;
        }
        return false;
    }
}