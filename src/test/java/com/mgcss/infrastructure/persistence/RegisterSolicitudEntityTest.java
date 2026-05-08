package com.mgcss.infrastructure.persistence;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Solicitud;

public class RegisterSolicitudEntityTest {

    private RegisterSolicitudEntity registerSolicitud;

    @BeforeEach
    void setUp() {
        registerSolicitud = new RegisterSolicitudEntity();
    }

    @Test
    void testDefaultConstructor() {
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity();
        assertNotNull(entity);
    }

    @Test
    void testParametrizedConstructor() {
        Long solicitudId = 1L;
        Solicitud.Estado estadoAnterior = Solicitud.Estado.ABIERTA;
        Solicitud.Estado estadoActual = Solicitud.Estado.EN_PROCESO;

        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(solicitudId, estadoAnterior, estadoActual);

        assertEquals(solicitudId, entity.getSolicitudId());
        assertEquals(estadoAnterior, entity.getEstadoAnterior());
        assertEquals(estadoActual, entity.getEstadoActual());
        assertNotNull(entity.getFechaHora());
    }

    @Test
    void testGetId() {
        assertNull(registerSolicitud.getId());
    }

    @Test
    void testGetSolicitudId() {
        Long solicitudId = 5L;
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(solicitudId, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        
        assertEquals(solicitudId, entity.getSolicitudId());
    }

    @Test
    void testGetFechaHora() {
        LocalDateTime fechaHoraAntes = LocalDateTime.now();
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        LocalDateTime fechaHoraDespues = LocalDateTime.now();

        assertNotNull(entity.getFechaHora());
        assertTrue(entity.getFechaHora().isAfter(fechaHoraAntes.minusSeconds(1)));
        assertTrue(entity.getFechaHora().isBefore(fechaHoraDespues.plusSeconds(1)));
    }

    @Test
    void testGetEstadoAnterior() {
        Solicitud.Estado estadoAnterior = Solicitud.Estado.ABIERTA;
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(1L, estadoAnterior, Solicitud.Estado.EN_PROCESO);
        
        assertEquals(estadoAnterior, entity.getEstadoAnterior());
    }

    @Test
    void testGetEstadoActual() {
        Solicitud.Estado estadoActual = Solicitud.Estado.CERRADA;
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(1L, Solicitud.Estado.EN_PROCESO, estadoActual);
        
        assertEquals(estadoActual, entity.getEstadoActual());
    }

    @Test
    void testStateTransitionAbiertaToEnProceso() {
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        
        assertEquals(Solicitud.Estado.ABIERTA, entity.getEstadoAnterior());
        assertEquals(Solicitud.Estado.EN_PROCESO, entity.getEstadoActual());
    }

    @Test
    void testStateTransitionEnProcesoToCerrada() {
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(2L, Solicitud.Estado.EN_PROCESO, Solicitud.Estado.CERRADA);
        
        assertEquals(Solicitud.Estado.EN_PROCESO, entity.getEstadoAnterior());
        assertEquals(Solicitud.Estado.CERRADA, entity.getEstadoActual());
    }

    @Test
    void testStateTransitionAbiertaToCerrada() {
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(3L, Solicitud.Estado.ABIERTA, Solicitud.Estado.CERRADA);
        
        assertEquals(Solicitud.Estado.ABIERTA, entity.getEstadoAnterior());
        assertEquals(Solicitud.Estado.CERRADA, entity.getEstadoActual());
    }

    @Test
    void testMultipleSolicitudIds() {
        RegisterSolicitudEntity entity1 = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        RegisterSolicitudEntity entity2 = new RegisterSolicitudEntity(2L, Solicitud.Estado.EN_PROCESO, Solicitud.Estado.CERRADA);
        
        assertEquals(1L, entity1.getSolicitudId());
        assertEquals(2L, entity2.getSolicitudId());
        assertNotEquals(entity1.getSolicitudId(), entity2.getSolicitudId());
    }

    @Test
    void testFechaHoraIsSetOnConstruction() {
        LocalDateTime before = LocalDateTime.now();
        RegisterSolicitudEntity entity = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(entity.getFechaHora());
        assertTrue(entity.getFechaHora().isAfter(before.minusSeconds(1)));
        assertTrue(entity.getFechaHora().isBefore(after.plusSeconds(1)));
    }
}
