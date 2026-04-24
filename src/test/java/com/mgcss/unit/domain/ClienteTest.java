package com.mgcss.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Cliente;

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Inicializamos un cliente base (usando el constructor sin ID) para las pruebas
        cliente = new Cliente("Maria", "maria@ejemplo.com", Cliente.TipoCliente.STANDARD);
    }

    @Test
    void testConstructorSinId_InicializaCorrectamente() {
        Cliente nuevoCliente = new Cliente("Carlos", "carlos@ejemplo.com", Cliente.TipoCliente.PREMIUM);

        assertNull(nuevoCliente.getId(), "El ID debe ser nulo al instanciar con el constructor de 3 parámetros");
        assertEquals("Carlos", nuevoCliente.getNombre(), "El nombre no coincide con el asignado en el constructor");
        assertEquals("carlos@ejemplo.com", nuevoCliente.getEmail(), "El email no coincide con el asignado");
        assertEquals(Cliente.TipoCliente.PREMIUM, nuevoCliente.getTipoCliente(), "El tipo de cliente no coincide");
    }

    @Test
    void testConstructorConId_InicializaCorrectamente() {
        Cliente clienteConId = new Cliente(1L, "Laura", "laura@ejemplo.com", Cliente.TipoCliente.STANDARD);

        assertEquals(1L, clienteConId.getId(), "El ID no coincide con el asignado en el constructor completo");
        assertEquals("Laura", clienteConId.getNombre(), "El nombre no coincide con el asignado");
        assertEquals("laura@ejemplo.com", clienteConId.getEmail(), "El email no coincide con el asignado");
        assertEquals(Cliente.TipoCliente.STANDARD, clienteConId.getTipoCliente(), "El tipo de cliente no coincide");
    }

    @Test
    void testModificarAtributos_GettersYSetters() {
        // Act: Modificamos los valores del cliente creado en el setUp
        cliente.setNombre("Maria Antonieta");
        cliente.setEmail("mantonieta@ejemplo.com");
        cliente.setTipoCliente(Cliente.TipoCliente.PREMIUM);

        // Assert: Verificamos que los getters devuelven los nuevos valores actualizados
        assertEquals("Maria Antonieta", cliente.getNombre(), "El nombre modificado no se guardó correctamente");
        assertEquals("mantonieta@ejemplo.com", cliente.getEmail(), "El email modificado no se guardó correctamente");
        assertEquals(Cliente.TipoCliente.PREMIUM, cliente.getTipoCliente(), "El tipo de cliente modificado no se guardó correctamente");
    }
}