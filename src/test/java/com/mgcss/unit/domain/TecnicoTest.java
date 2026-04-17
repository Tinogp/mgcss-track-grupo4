package com.mgcss.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Tecnico;

public class TecnicoTest {

    private Tecnico tecnico;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        tecnico = new Tecnico("Juan", Tecnico.Especialidad.REDES);
    }

    @Test
    void testConstructor_InicializaCorrectamente() {
        assertEquals("Juan", tecnico.getNombre(), "El nombre no coincide con el asignado en el constructor");
        assertEquals(Tecnico.Especialidad.REDES, tecnico.getEspecialidad(), "La especialidad no coincide");
        assertFalse(tecnico.isActivo(), "Un técnico debe inicializarse siempre como inactivo por defecto");
        assertNull(tecnico.getId(), "El ID debe ser nulo al instanciar hasta que se asigne explícitamente");
    }

    @Test
    void testActivar_DesdeInactivo_Exito() {
        boolean resultado = tecnico.activar();

        assertTrue(resultado, "Debería devolver true al activar un técnico inactivo");
        assertTrue(tecnico.isActivo(), "El estado del técnico debe cambiar a activo");
    }

    @Test
    void testActivar_YaActivo_Falla() {
        tecnico.activar();

        boolean resultado = tecnico.activar();

        assertFalse(resultado, "Debería devolver false porque el técnico ya estaba activo");
        assertTrue(tecnico.isActivo(), "El estado del técnico debe mantenerse activo");
    }

    @Test
    void testDesactivar_DesdeActivo_Exito() {
        tecnico.activar();

        boolean resultado = tecnico.desactivar();

        assertTrue(resultado, "Debería devolver true al desactivar un técnico activo");
        assertFalse(tecnico.isActivo(), "El estado del técnico debe cambiar a inactivo");
    }

    @Test
    void testDesactivar_YaInactivo_Falla() {

        boolean resultado = tecnico.desactivar();

        assertFalse(resultado, "Debería devolver false porque el técnico ya estaba inactivo");
        assertFalse(tecnico.isActivo(), "El estado del técnico debe mantenerse inactivo");
    }

    @Test
    void testModificarAtributos_GettersYSetters() {
        tecnico.setId(100L);
        tecnico.setNombre("Pedro");
        tecnico.setEspecialidad(Tecnico.Especialidad.SOFTWARE);

        assertEquals(100L, tecnico.getId());
        assertEquals("Pedro", tecnico.getNombre());
        assertEquals(Tecnico.Especialidad.SOFTWARE, tecnico.getEspecialidad());
    }
}
