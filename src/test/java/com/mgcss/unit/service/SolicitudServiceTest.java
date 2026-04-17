package com.mgcss.unit.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;

public class SolicitudServiceTest {

    private SolicitudRepository solicitudRepoMock;
    private TecnicoRepository tecnicoRepoMock;
    private SolicitudService solicitudService; // System Under Test

    @BeforeEach
    void setUp() {
        // 1. Arranque: Creamos los "actores de reparto" falsos
        solicitudRepoMock = mock(SolicitudRepository.class);
        tecnicoRepoMock = mock(TecnicoRepository.class);

        // Inyectamos los mocks en el servicio real
        solicitudService = new SolicitudService(solicitudRepoMock, tecnicoRepoMock);
    }

    @Test
    void debeAsignarTecnicoCorrectamente_YGuardarCambios() {
        // Arranque: Preparamos los datos puros del dominio
        Solicitud solicitud = new Solicitud(); 
        Tecnico tecnico = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnico.activar();

        // Configuramos el universo simulado: qué deben responder los repositorios
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        when(tecnicoRepoMock.findById(99L)).thenReturn(Optional.of(tecnico));

        // 2. Act: Ejecutamos el orquestador
        boolean resultado = solicitudService.asignarTecnico(1L, 99L);

        // 3. Assert & Verify: Comprobamos el comportamiento
        assertTrue(resultado, "El servicio debería devolver true al asignar correctamente");
        
        // Verificamos que el servicio mandó a guardar la solicitud actualizada a la BD simulada
        verify(solicitudRepoMock).save(solicitud);
    }

    @Test
    void asignarTecnico_SiSolicitudNoExiste_LanzaExcepcion() {
        // 1. Arrange: Simulamos que la BD está vacía para esa solicitud
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.empty());

        // 2. Act & Assert: Verificamos que explota con la excepción correcta
        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.asignarTecnico(1L, 99L);
        }, "Debería lanzar IllegalArgumentException si la solicitud no existe");

        // 3. Verify: Confirmamos que NUNCA se intentó guardar nada (muy importante)
        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void asignarTecnico_SiTecnicoNoExiste_LanzaExcepcion() {
        Solicitud solicitud = new Solicitud();
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        when(tecnicoRepoMock.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.asignarTecnico(1L, 99L);
        }, "Debería lanzar IllegalArgumentException si el técnico no existe");

        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void asignarTecnico_SiTecnicoInactivo_NoGuarda() {
        Solicitud solicitud = new Solicitud();
        Tecnico tecnicoInactivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);

        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        when(tecnicoRepoMock.findById(99L)).thenReturn(Optional.of(tecnicoInactivo));

        boolean resultado = solicitudService.asignarTecnico(1L, 99L);

        assertFalse(resultado, "Debería devolver false cuando el técnico está inactivo");
        verify(solicitudRepoMock, never()).save(any());
    }
}