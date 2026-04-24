package com.mgcss.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.domain.Cliente;

public class SolicitudTest {

    private Solicitud solicitud;
    private Tecnico tecnicoActivo;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        solicitud = new Solicitud(new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.STANDARD));
        tecnicoActivo = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnicoActivo.activar();
    }

    @Test
    void testSetDescripcion_EstadoAbierta_Exito() {
        boolean modificado = solicitud.setDescripcion("Problema con el monitor");

        assertTrue(modificado, "Debería permitir cambiar la descripción si está ABIERTA");
        assertEquals("Problema con el monitor", solicitud.getDescripcion());
    }

    @Test
    void testSetDescripcion_EstadoCerrada_Falla() {
        solicitud.asignarTecnico(tecnicoActivo);
        solicitud.iniciarProceso();
        solicitud.cerrar();

        boolean modificado = solicitud.setDescripcion("Intento de cambio tardío");

        assertFalse(modificado, "No debería permitir cambiar la descripción si está CERRADA");
        assertNull(solicitud.getDescripcion(), "La descripción debe seguir siendo la original (null en este caso)");
    }

    @Test
    void testAsignarTecnico_ConTecnicoActivo() {
        boolean asignado = solicitud.asignarTecnico(tecnicoActivo);

        assertTrue(asignado, "Debería poder asignar un técnico activo a una solicitud ABIERTA");
        assertEquals(tecnicoActivo, solicitud.getTecnicoAsignado(), "El técnico debe quedar asignado internamente");
    }

    @Test
    void testAsignarTecnico_ConTecnicoInactivo() {
        Tecnico tecnicoInactivo = new Tecnico("Carlos", Tecnico.Especialidad.SOFTWARE);

        boolean asignado = solicitud.asignarTecnico(tecnicoInactivo);

        assertFalse(asignado, "No debería asignar el técnico si no está activo");
        assertNull(solicitud.getTecnicoAsignado(), "El técnico asignado debe seguir siendo null");
    }

    @Test
    void testIniciarProceso_ConTecnicoAsignado_Exito() {
        solicitud.asignarTecnico(tecnicoActivo);

        boolean iniciado = solicitud.iniciarProceso();

        assertTrue(iniciado, "Debería poder iniciar el proceso si hay un técnico asignado");
        assertEquals(Solicitud.Estado.EN_PROCESO, solicitud.getEstado(), "El estado debe cambiar a EN_PROCESO");
    }

    @Test
    void testIniciarProceso_SinTecnicoAsignado_Falla() {
        boolean iniciado = solicitud.iniciarProceso();

        assertFalse(iniciado, "No debería iniciar el proceso si no hay técnico asignado");
        assertEquals(Solicitud.Estado.ABIERTA, solicitud.getEstado(), "El estado debe mantenerse ABIERTA");
    }

    @Test
    void testCerrar_DesdeEstadoAbierta_Falla() {
        assertEquals(Solicitud.Estado.ABIERTA, solicitud.getEstado());

        boolean cerrado = solicitud.cerrar();

        assertFalse(cerrado, "No debería poder cerrar una solicitud que no esté EN_PROCESO");
        assertEquals(Solicitud.Estado.ABIERTA, solicitud.getEstado(), "El estado debe seguir siendo ABIERTA");
        assertNull(solicitud.getFechaCierre(), "La fecha de cierre debe ser null");
    }

    @Test
    void testCerrar_DesdeEstadoEnProceso_Exito() {
        solicitud.asignarTecnico(tecnicoActivo);
        solicitud.iniciarProceso();

        boolean cerrado = solicitud.cerrar();

        assertTrue(cerrado, "Debería poder cerrar la solicitud si estaba EN_PROCESO");
        assertEquals(Solicitud.Estado.CERRADA, solicitud.getEstado(), "El estado debe cambiar a CERRADA");
        assertNotNull(solicitud.getFechaCierre(), "La fecha de cierre debe haberse registrado");
    }
}
