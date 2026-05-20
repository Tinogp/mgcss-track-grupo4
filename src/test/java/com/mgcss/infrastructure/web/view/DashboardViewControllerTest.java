package com.mgcss.infrastructure.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.service.ClienteService;
import com.mgcss.service.SolicitudService;
import com.mgcss.service.TecnicoService;

class DashboardViewControllerTest {

    private static final String MESSAGE_ATTRIBUTE = "mensaje";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String SOLICITUDES_REDIRECT = "redirect:/ui#solicitudes";

    private ClienteService clienteService;
    private SolicitudService solicitudService;
    private TecnicoService tecnicoService;
    private DashboardViewController controller;

    @BeforeEach
    void setUp() {
        clienteService = mock(ClienteService.class);
        solicitudService = mock(SolicitudService.class);
        tecnicoService = mock(TecnicoService.class);
        controller = new DashboardViewController(clienteService, solicitudService, tecnicoService);
    }

    @Test
    void dashboardCargaDatosResumenListasYSolicitudConsultada() {
        Cliente cliente = new Cliente(1L, "Cliente Uno", "cliente@test.com", Cliente.TipoCliente.PREMIUM);
        Tecnico activo = new Tecnico(1L, "Ana Soporte", Tecnico.Especialidad.SOFTWARE, true);
        Tecnico inactivo = new Tecnico(2L, "Luis Redes", Tecnico.Especialidad.REDES, false);
        Solicitud abierta = new Solicitud(1L, "Pantalla rota", cliente, LocalDateTime.now(), Solicitud.Estado.ABIERTA, null, null);
        Solicitud enProceso = new Solicitud(2L, "Instalar app", cliente, LocalDateTime.now(), Solicitud.Estado.EN_PROCESO, activo, null);
        Solicitud cerrada = new Solicitud(3L, "Cableado", cliente, LocalDateTime.now(), Solicitud.Estado.CERRADA, activo, LocalDateTime.now());

        when(clienteService.getAllCliente()).thenReturn(List.of(cliente));
        when(solicitudService.listarSolicitudes()).thenReturn(List.of(abierta, enProceso, cerrada));
        when(tecnicoService.listarTecnicos()).thenReturn(List.of(activo, inactivo));
        when(solicitudService.obtenerSolicitud(2L)).thenReturn(Optional.of(enProceso));

        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.dashboard(2L, model);

        assertThat(view).isEqualTo("dashboard");
        assertThat(model)
                .containsEntry("clientes", List.of(cliente))
                .containsEntry("solicitudes", List.of(abierta, enProceso, cerrada))
                .containsEntry("tecnicos", List.of(activo, inactivo))
                .containsEntry("tecnicosActivosLista", List.of(activo))
                .containsEntry("solicitudConsultada", enProceso)
                .containsEntry("solicitudConsultadaId", 2L)
                .containsEntry("totalClientes", 1)
                .containsEntry("totalSolicitudes", 3)
                .containsEntry("totalTecnicos", 2)
                .containsEntry("tecnicosActivos", 1L)
                .containsEntry("solicitudesAbiertas", 1L)
                .containsEntry("solicitudesEnProceso", 1L)
                .containsEntry("solicitudesCerradas", 1L);
        assertThat((Cliente.TipoCliente[]) model.get("tiposCliente")).containsExactly(Cliente.TipoCliente.values());
        assertThat((Tecnico.Especialidad[]) model.get("especialidades")).containsExactly(Tecnico.Especialidad.values());
    }

    @Test
    void dashboardSinIdConsultadoNoBuscaSolicitud() {
        when(clienteService.getAllCliente()).thenReturn(List.of());
        when(solicitudService.listarSolicitudes()).thenReturn(List.of());
        when(tecnicoService.listarTecnicos()).thenReturn(List.of());

        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.dashboard(null, model);

        assertThat(view).isEqualTo("dashboard");
        assertThat(model)
                .containsEntry("solicitudConsultada", null)
                .containsEntry("solicitudConsultadaId", null);
    }

    @Test
    void crearClienteRedirigeYAnadeMensaje() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String result = controller.crearCliente("ACME", "acme@test.com", Cliente.TipoCliente.STANDARD, redirect);

        assertThat(result).isEqualTo("redirect:/ui");
        verify(clienteService).crearCliente("ACME", "acme@test.com", Cliente.TipoCliente.STANDARD);
        assertFlash(redirect, MESSAGE_ATTRIBUTE, "Cliente creado correctamente.");
    }

    @Test
    void crearSolicitudCubreExitoYError() {
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();
        String ok = controller.crearSolicitud("Incidencia", 1L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui");
        verify(solicitudService).crearSolicitud("Incidencia", 1L);
        assertFlash(okRedirect, MESSAGE_ATTRIBUTE, "Solicitud registrada correctamente.");

        when(solicitudService.crearSolicitud("Mala", 99L)).thenThrow(new IllegalArgumentException("Cliente inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.crearSolicitud("Mala", 99L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui");
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "Cliente inexistente");
    }

    @Test
    void crearYActivarTecnicoCubrenExitoYError() {
        RedirectAttributesModelMap createRedirect = new RedirectAttributesModelMap();

        String create = controller.crearTecnico("Tecnico Uno", Tecnico.Especialidad.HARDWARE, createRedirect);

        assertThat(create).isEqualTo("redirect:/ui");
        verify(tecnicoService).crearTecnico(any(Tecnico.class));
        assertFlash(createRedirect, MESSAGE_ATTRIBUTE, "Tecnico creado correctamente.");

        when(tecnicoService.activarTecnico(1L)).thenReturn(true);
        RedirectAttributesModelMap activeRedirect = new RedirectAttributesModelMap();

        String active = controller.activarTecnico(1L, activeRedirect);

        assertThat(active).isEqualTo("redirect:/ui");
        assertFlash(activeRedirect, MESSAGE_ATTRIBUTE, "Tecnico activado.");

        when(tecnicoService.activarTecnico(2L)).thenReturn(false);
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.activarTecnico(2L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui");
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "No se pudo activar el tecnico.");
    }

    @Test
    void asignarTecnicoCubreExitoFalloYExcepcion() {
        when(solicitudService.asignarTecnico(1L, 10L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.asignarTecnico(1L, 10L, okRedirect);

        assertThat(ok).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(okRedirect, MESSAGE_ATTRIBUTE, "Tecnico asignado a la solicitud.");

        when(solicitudService.asignarTecnico(2L, 10L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.asignarTecnico(2L, 10L, failRedirect);

        assertThat(fail).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(failRedirect, ERROR_ATTRIBUTE, "No se pudo asignar el tecnico.");

        when(solicitudService.asignarTecnico(3L, 10L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.asignarTecnico(3L, 10L, errorRedirect);

        assertThat(error).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "Solicitud inexistente");
    }

    @Test
    void iniciarProcesoCubreExitoFalloYExcepcion() {
        when(solicitudService.iniciarProceso(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.iniciarProceso(1L, okRedirect);

        assertThat(ok).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(okRedirect, MESSAGE_ATTRIBUTE, "Solicitud puesta en proceso.");

        when(solicitudService.iniciarProceso(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.iniciarProceso(2L, failRedirect);

        assertThat(fail).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(failRedirect, ERROR_ATTRIBUTE, "La solicitud necesita un tecnico asignado para iniciarse.");

        when(solicitudService.iniciarProceso(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.iniciarProceso(3L, errorRedirect);

        assertThat(error).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "Solicitud inexistente");
    }

    @Test
    void cerrarSolicitudCubreExitoFalloYExcepcion() {
        when(solicitudService.cerrarSolicitud(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.cerrarSolicitud(1L, okRedirect);

        assertThat(ok).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(okRedirect, MESSAGE_ATTRIBUTE, "Solicitud cerrada.");

        when(solicitudService.cerrarSolicitud(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.cerrarSolicitud(2L, failRedirect);

        assertThat(fail).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(failRedirect, ERROR_ATTRIBUTE, "La solicitud no se puede cerrar en su estado actual.");

        when(solicitudService.cerrarSolicitud(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.cerrarSolicitud(3L, errorRedirect);

        assertThat(error).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "Solicitud inexistente");
    }

    @Test
    void reabrirSolicitudCubreExitoFalloYExcepcion() {
        when(solicitudService.reabrirSolicitud(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.reabrirSolicitud(1L, okRedirect);

        assertThat(ok).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(okRedirect, MESSAGE_ATTRIBUTE, "Solicitud reabierta.");

        when(solicitudService.reabrirSolicitud(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.reabrirSolicitud(2L, failRedirect);

        assertThat(fail).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(failRedirect, ERROR_ATTRIBUTE, "La solicitud no se puede reabrir en su estado actual.");

        when(solicitudService.reabrirSolicitud(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.reabrirSolicitud(3L, errorRedirect);

        assertThat(error).isEqualTo(SOLICITUDES_REDIRECT);
        assertFlash(errorRedirect, ERROR_ATTRIBUTE, "Solicitud inexistente");
    }

    private void assertFlash(RedirectAttributesModelMap redirectAttributes, String key, String value) {
        Map<String, Object> flashAttributes = new LinkedHashMap<>();
        redirectAttributes.getFlashAttributes().forEach(flashAttributes::put);
        assertThat(flashAttributes).containsEntry(key, value);
    }
}
