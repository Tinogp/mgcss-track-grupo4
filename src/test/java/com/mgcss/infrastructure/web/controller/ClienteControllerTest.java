package com.mgcss.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.web.dto.ClienteRequestDTO;
import com.mgcss.service.ClienteService;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearCliente() throws Exception {
        // Datos de entrada
        ClienteRequestDTO request = new ClienteRequestDTO("Empresa ACME", "contacto@acme.com", Cliente.TipoCliente.PREMIUM);
        
        // Mock del dominio que devolvería el servicio
        Cliente mockCliente = new Cliente(10L, "Empresa ACME", "contacto@acme.com", Cliente.TipoCliente.PREMIUM);

        // Definir comportamiento del Mock
        Mockito.when(clienteService.crearCliente(anyString(), anyString(), any(Cliente.TipoCliente.class)))
               .thenReturn(mockCliente);

        // Ejecutar y Validar
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Empresa ACME"))
                .andExpect(jsonPath("$.tipoCliente").value("PREMIUM"));
    }
}