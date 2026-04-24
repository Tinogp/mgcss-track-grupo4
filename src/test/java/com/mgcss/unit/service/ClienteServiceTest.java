package com.mgcss.unit.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.service.ClienteService;

public class ClienteServiceTest {

    private ClienteRepository clienteRepositoryMock;
    private ClienteService clienteService; // System Under Test (SUT)

    @BeforeEach
    void setUp() {
        // 1. Arranque: Creamos el mock del repositorio
        clienteRepositoryMock = mock(ClienteRepository.class);

        // Inyectamos el mock en el servicio real
        clienteService = new ClienteService(clienteRepositoryMock);
    }

    @Test
    void createCliente_GuardaYDevuelveElCliente() {
        // Arrange
        Cliente cliente = mock(Cliente.class);
        when(clienteRepositoryMock.save(cliente)).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.createCliente(cliente);

        // Assert & Verify
        assertNotNull(resultado, "Debería devolver el cliente creado");
        verify(clienteRepositoryMock).save(cliente);
    }

    @Test
    void deleteCliente_SiClienteExiste_LoEliminaYDevuelveTrue() {
        // Arrange: Simulamos que el cliente SÍ existe en la base de datos
        Cliente cliente = mock(Cliente.class);
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        boolean resultado = clienteService.deleteCliente(1L);

        // Assert & Verify
        assertTrue(resultado, "Debería devolver true al eliminar exitosamente");
        verify(clienteRepositoryMock).deleteById(1L);
    }

    @Test
    void deleteCliente_SiClienteNoExiste_DevuelveFalseYNoElimina() {
        // Arrange: Simulamos que el cliente NO existe (Optional vacío)
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = clienteService.deleteCliente(1L);

        // Assert & Verify
        assertFalse(resultado, "Debería devolver false porque no se encontró el cliente a eliminar");
        verify(clienteRepositoryMock, never()).deleteById(any());
    }

    @Test
    void updateCliente_SiClienteExiste_LoActualizaYDevuelveTrue() {
        // Arrange
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(1L); // Simulamos que el cliente tiene el ID 1
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        boolean resultado = clienteService.updateCliente(cliente);

        // Assert & Verify
        assertTrue(resultado, "Debería devolver true al actualizar exitosamente");
        verify(clienteRepositoryMock).save(cliente);
    }

    @Test
    void updateCliente_SiClienteNoExiste_DevuelveFalseYNoGuarda() {
        // Arrange
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(1L);
        when(clienteRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean resultado = clienteService.updateCliente(cliente);

        // Assert & Verify
        assertFalse(resultado, "Debería devolver false porque no se encontró el cliente para actualizar");
        // Aseguramos que NUNCA se guarde un cliente que no existe previamente
        verify(clienteRepositoryMock, never()).save(any()); 
    }

    @Test
    void getAllCliente_DevuelveLaListaDeClientes() {
        // Arrange
        Cliente cliente1 = mock(Cliente.class);
        Cliente cliente2 = mock(Cliente.class);
        List<Cliente> listaSimulada = Arrays.asList(cliente1, cliente2);
        
        when(clienteRepositoryMock.findAll()).thenReturn(listaSimulada);

        // Act
        List<Cliente> resultado = clienteService.getAllCliente();

        // Assert & Verify
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debería devolver una lista con 2 clientes");
        verify(clienteRepositoryMock).findAll();
    }
}