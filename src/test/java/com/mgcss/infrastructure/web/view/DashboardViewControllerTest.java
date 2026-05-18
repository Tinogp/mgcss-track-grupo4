package com.mgcss.infrastructure.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
        assertThat(model.get("clientes")).isEqualTo(List.of(cliente));
        assertThat(model.get("solicitudes")).isEqualTo(List.of(abierta, enProceso, cerrada));
        assertThat(model.get("tecnicos")).isEqualTo(List.of(activo, inactivo));
        assertThat(model.get("tecnicosActivosLista")).isEqualTo(List.of(activo));
        assertThat(model.get("solicitudConsultada")).isEqualTo(enProceso);
        assertThat(model.get("solicitudConsultadaId")).isEqualTo(2L);
        assertThat(model.get("totalClientes")).isEqualTo(1);
        assertThat(model.get("totalSolicitudes")).isEqualTo(3);
        assertThat(model.get("totalTecnicos")).isEqualTo(2);
        assertThat(model.get("tecnicosActivos")).isEqualTo(1L);
        assertThat(model.get("solicitudesAbiertas")).isEqualTo(1L);
        assertThat(model.get("solicitudesEnProceso")).isEqualTo(1L);
        assertThat(model.get("solicitudesCerradas")).isEqualTo(1L);
        assertThat(model.get("tiposCliente")).isEqualTo(Cliente.TipoCliente.values());
        assertThat(model.get("especialidades")).isEqualTo(Tecnico.Especialidad.values());
    }

    @Test
    void dashboardSinIdConsultadoNoBuscaSolicitud() {
        when(clienteService.getAllCliente()).thenReturn(List.of());
        when(solicitudService.listarSolicitudes()).thenReturn(List.of());
        when(tecnicoService.listarTecnicos()).thenReturn(List.of());

        ExtendedModelMap model = new ExtendedModelMap();

        String view = controller.dashboard(null, model);

        assertThat(view).isEqualTo("dashboard");
        assertThat(model.get("solicitudConsultada")).isNull();
        assertThat(model.get("solicitudConsultadaId")).isNull();
    }

    @Test
    void crearClienteRedirigeYAnadeMensaje() {
        RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

        String result = controller.crearCliente("ACME", "acme@test.com", Cliente.TipoCliente.STANDARD, redirect);

        assertThat(result).isEqualTo("redirect:/ui");
        verify(clienteService).crearCliente("ACME", "acme@test.com", Cliente.TipoCliente.STANDARD);
        assertFlash(redirect, "mensaje", "Cliente creado correctamente.");
    }

    @Test
    void crearSolicitudCubreExitoYError() {
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();
        String ok = controller.crearSolicitud("Incidencia", 1L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui");
        verify(solicitudService).crearSolicitud("Incidencia", 1L);
        assertFlash(okRedirect, "mensaje", "Solicitud registrada correctamente.");

        when(solicitudService.crearSolicitud("Mala", 99L)).thenThrow(new IllegalArgumentException("Cliente inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.crearSolicitud("Mala", 99L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui");
        assertFlash(errorRedirect, "error", "Cliente inexistente");
    }

    @Test
    void crearYActivarTecnicoCubrenExitoYError() {
        RedirectAttributesModelMap createRedirect = new RedirectAttributesModelMap();

        String create = controller.crearTecnico("Tecnico Uno", Tecnico.Especialidad.HARDWARE, createRedirect);

        assertThat(create).isEqualTo("redirect:/ui");
        verify(tecnicoService).crearTecnico(any(Tecnico.class));
        assertFlash(createRedirect, "mensaje", "Tecnico creado correctamente.");

        when(tecnicoService.activarTecnico(1L)).thenReturn(true);
        RedirectAttributesModelMap activeRedirect = new RedirectAttributesModelMap();

        String active = controller.activarTecnico(1L, activeRedirect);

        assertThat(active).isEqualTo("redirect:/ui");
        assertFlash(activeRedirect, "mensaje", "Tecnico activado.");

        when(tecnicoService.activarTecnico(2L)).thenReturn(false);
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.activarTecnico(2L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui");
        assertFlash(errorRedirect, "error", "No se pudo activar el tecnico.");
    }

    @Test
    void asignarTecnicoCubreExitoFalloYExcepcion() {
        when(solicitudService.asignarTecnico(1L, 10L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.asignarTecnico(1L, 10L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(okRedirect, "mensaje", "Tecnico asignado a la solicitud.");

        when(solicitudService.asignarTecnico(2L, 10L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.asignarTecnico(2L, 10L, failRedirect);

        assertThat(fail).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(failRedirect, "error", "No se pudo asignar el tecnico.");

        when(solicitudService.asignarTecnico(3L, 10L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.asignarTecnico(3L, 10L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(errorRedirect, "error", "Solicitud inexistente");
    }

    @Test
    void iniciarProcesoCubreExitoFalloYExcepcion() {
        when(solicitudService.iniciarProceso(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.iniciarProceso(1L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(okRedirect, "mensaje", "Solicitud puesta en proceso.");

        when(solicitudService.iniciarProceso(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.iniciarProceso(2L, failRedirect);

        assertThat(fail).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(failRedirect, "error", "La solicitud necesita un tecnico asignado para iniciarse.");

        when(solicitudService.iniciarProceso(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.iniciarProceso(3L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(errorRedirect, "error", "Solicitud inexistente");
    }

    @Test
    void cerrarSolicitudCubreExitoFalloYExcepcion() {
        when(solicitudService.cerrarSolicitud(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.cerrarSolicitud(1L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(okRedirect, "mensaje", "Solicitud cerrada.");

        when(solicitudService.cerrarSolicitud(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.cerrarSolicitud(2L, failRedirect);

        assertThat(fail).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(failRedirect, "error", "La solicitud no se puede cerrar en su estado actual.");

        when(solicitudService.cerrarSolicitud(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.cerrarSolicitud(3L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(errorRedirect, "error", "Solicitud inexistente");
    }

    @Test
    void reabrirSolicitudCubreExitoFalloYExcepcion() {
        when(solicitudService.reabrirSolicitud(1L)).thenReturn(true);
        RedirectAttributesModelMap okRedirect = new RedirectAttributesModelMap();

        String ok = controller.reabrirSolicitud(1L, okRedirect);

        assertThat(ok).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(okRedirect, "mensaje", "Solicitud reabierta.");

        when(solicitudService.reabrirSolicitud(2L)).thenReturn(false);
        RedirectAttributesModelMap failRedirect = new RedirectAttributesModelMap();

        String fail = controller.reabrirSolicitud(2L, failRedirect);

        assertThat(fail).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(failRedirect, "error", "La solicitud no se puede reabrir en su estado actual.");

        when(solicitudService.reabrirSolicitud(3L)).thenThrow(new IllegalArgumentException("Solicitud inexistente"));
        RedirectAttributesModelMap errorRedirect = new RedirectAttributesModelMap();

        String error = controller.reabrirSolicitud(3L, errorRedirect);

        assertThat(error).isEqualTo("redirect:/ui#solicitudes");
        assertFlash(errorRedirect, "error", "Solicitud inexistente");
    }

    private void assertFlash(RedirectAttributesModelMap redirectAttributes, String key, String value) {
        Map<String, ?> flashAttributes = redirectAttributes.getFlashAttributes();
        assertThat(flashAttributes.get(key)).isEqualTo(value);
    }
}
