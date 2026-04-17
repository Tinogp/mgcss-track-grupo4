package com.mgcss.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mgcss.domain.Cliente;
import org.junit.jupiter.api.Test;

public class ClienteTest {

    @Test
    void debeCrearClientePremiumCorrectamente() {
        Cliente cliente = new Cliente("Juan", "[EMAIL_ADDRESS]", Cliente.TipoCliente.PREMIUM);

        assertNotNull(cliente);
        assertEquals("Juan", cliente.getNombre());
        assertEquals("[EMAIL_ADDRESS]", cliente.getEmail());
        assertEquals(Cliente.TipoCliente.PREMIUM, cliente.getTipoCliente());
    }
}
