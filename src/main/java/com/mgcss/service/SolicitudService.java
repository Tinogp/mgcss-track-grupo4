package com.mgcss.service;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.RegisterSolicitudRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

import java.util.List;
import java.util.Optional;

public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final RegisterSolicitudRepository registerSolicitudRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository,
            TecnicoRepository tecnicoRepository,
            RegisterSolicitudRepository registerSolicitudRepository,
            ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.registerSolicitudRepository = registerSolicitudRepository;
        this.clienteRepository = clienteRepository;
    }

    public Solicitud crearSolicitud(String descripcion, Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente con ID " + clienteId + " no existe"));
        
        Solicitud solicitud = new Solicitud(cliente);
        solicitud.setDescripcion(descripcion);
        return solicitudRepository.save(solicitud);
    }

    public Optional<Solicitud> obtenerSolicitud(Long solicitudId) {
        return solicitudRepository.findById(solicitudId);
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
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