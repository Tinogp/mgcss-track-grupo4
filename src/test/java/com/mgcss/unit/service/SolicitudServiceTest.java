package com.mgcss.unit.service;

import java.time.LocalDateTime;
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

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.RegisterSolicitudRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;
import com.mgcss.service.SolicitudService;

class SolicitudServiceTest {

    private SolicitudRepository solicitudRepoMock;
    private TecnicoRepository tecnicoRepoMock;
    private RegisterSolicitudRepository registerSolicitudRepoMock;
    private ClienteRepository clienteRepoMock;
    private SolicitudService solicitudService; // System Under Test

    @BeforeEach
    void setUp() {
        // 1. Arranque: Creamos los "actores de reparto" falsos
        solicitudRepoMock = mock(SolicitudRepository.class);
        tecnicoRepoMock = mock(TecnicoRepository.class);
        registerSolicitudRepoMock = mock(RegisterSolicitudRepository.class);
        clienteRepoMock = mock(ClienteRepository.class);

        // Inyectamos los mocks en el servicio real
        solicitudService = new SolicitudService(solicitudRepoMock, tecnicoRepoMock, registerSolicitudRepoMock, clienteRepoMock);
    }

    // ========== TESTS PARA asignarTecnico() ==========

    @Test
    void debeAsignarTecnicoCorrectamente_YGuardarCambios() {
        // Arranque: Preparamos los datos puros del dominio
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD)); 
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
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        when(tecnicoRepoMock.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.asignarTecnico(1L, 99L);
        }, "Debería lanzar IllegalArgumentException si el técnico no existe");

        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void asignarTecnico_SiTecnicoInactivo_NoGuarda() {
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        Tecnico tecnicoInactivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);

        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        when(tecnicoRepoMock.findById(99L)).thenReturn(Optional.of(tecnicoInactivo));

        boolean resultado = solicitudService.asignarTecnico(1L, 99L);

        assertFalse(resultado, "Debería devolver false cuando el técnico está inactivo");
        verify(solicitudRepoMock, never()).save(any());
    }

    // ========== TESTS PARA iniciarProceso() ==========

    @Test
    void iniciarProceso_Exitoso_GuardaSolicitudYRegistro() {
        // Arrange
        Tecnico tecnicoActivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnicoActivo.activar();
        Solicitud solicitud = new Solicitud(
            1L,
            null,
            new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD),
            LocalDateTime.now(),
            Solicitud.Estado.ABIERTA,
            tecnicoActivo,
            null
        );
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        
        // Act
        boolean resultado = solicitudService.iniciarProceso(1L);

        // Assert
        assertTrue(resultado, "Debería devolver true al iniciar el proceso correctamente");
        verify(solicitudRepoMock).save(solicitud);
        verify(registerSolicitudRepoMock).save(any(RegisterSolicitudEntity.class));
    }

    @Test
    void iniciarProceso_SolicitudNoExiste_LanzaExcepcion() {
        // Arrange
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.iniciarProceso(1L);
        }, "Debería lanzar IllegalArgumentException si la solicitud no existe");

        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void iniciarProceso_SiNoSePuedeIniciar_NoGuarda() {
        // Arrange
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        // Aquí la solicitud.iniciarProcesoConRegistro() retorna null

        // Act
        boolean resultado = solicitudService.iniciarProceso(1L);

        // Assert
        assertFalse(resultado, "Debería devolver false si no se puede iniciar el proceso");
        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }

    // ========== TESTS PARA cerrarSolicitud() ==========

    @Test
    void cerrarSolicitud_Exitoso_GuardaSolicitudYRegistro() {
        // Arrange
        Tecnico tecnicoActivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnicoActivo.activar();
        Solicitud solicitud = new Solicitud(
            1L,
            null,
            new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD),
            LocalDateTime.now(),
            Solicitud.Estado.EN_PROCESO,
            tecnicoActivo,
            null
        );
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        
        // Act
        boolean resultado = solicitudService.cerrarSolicitud(1L);

        // Assert
        assertTrue(resultado, "Debería devolver true al cerrar la solicitud correctamente");
        verify(solicitudRepoMock).save(solicitud);
        verify(registerSolicitudRepoMock).save(any(RegisterSolicitudEntity.class));
    }

    @Test
    void cerrarSolicitud_SolicitudNoExiste_LanzaExcepcion() {
        // Arrange
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.cerrarSolicitud(1L);
        }, "Debería lanzar IllegalArgumentException si la solicitud no existe");

        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void cerrarSolicitud_SiNoSePuedeCerrar_NoGuarda() {
        // Arrange
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        // La solicitud.cerrarConRegistro() retorna null

        // Act
        boolean resultado = solicitudService.cerrarSolicitud(1L);

        // Assert
        assertFalse(resultado, "Debería devolver false si no se puede cerrar la solicitud");
        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }

    // ========== TESTS PARA reabrirSolicitud() ==========

    @Test
    void reabrirSolicitud_Exitoso_GuardaSolicitudYRegistro() {
        // Arrange
        Tecnico tecnicoActivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnicoActivo.activar();
        Solicitud solicitud = new Solicitud(
            1L,
            null,
            new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD),
            LocalDateTime.now(),
            Solicitud.Estado.CERRADA,
            tecnicoActivo,
            LocalDateTime.now()
        );
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        
        // Act
        boolean resultado = solicitudService.reabrirSolicitud(1L);

        // Assert
        assertTrue(resultado, "Debería devolver true al reabrir la solicitud correctamente");
        verify(solicitudRepoMock).save(solicitud);
        verify(registerSolicitudRepoMock).save(any(RegisterSolicitudEntity.class));
    }

    @Test
    void reabrirSolicitud_SolicitudNoExiste_LanzaExcepcion() {
        // Arrange
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            solicitudService.reabrirSolicitud(1L);
        }, "Debería lanzar IllegalArgumentException si la solicitud no existe");

        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }

    @Test
    void reabrirSolicitud_SiNoSePuedeReabrir_NoGuarda() {
        // Arrange
        Solicitud solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        
        when(solicitudRepoMock.findById(1L)).thenReturn(Optional.of(solicitud));
        // La solicitud.reabrirConRegistro() retorna null

        // Act
        boolean resultado = solicitudService.reabrirSolicitud(1L);

        // Assert
        assertFalse(resultado, "Debería devolver false si no se puede reabrir la solicitud");
        verify(registerSolicitudRepoMock, never()).save(any());
        verify(solicitudRepoMock, never()).save(any());
    }
}