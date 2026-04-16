package com.mgcss.service;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;

public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, TecnicoRepository tecnicoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public boolean asignarTecnico(Long solicitudId, Long tecnicoId) {
        
        // 1. Recuperamos y lanzamos excepción si no existen (Fail-Fast)
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("La solicitud con ID " + solicitudId + " no existe"));

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("El técnico con ID " + tecnicoId + " no existe"));

        // 2. Delegamos al dominio
        boolean asignado = solicitud.asignarTecnico(tecnico);

        // 3. Guardamos si fue exitoso
        if (asignado) {
            solicitudRepository.save(solicitud);
            return true;
        }
        
        return false; 
    }
}