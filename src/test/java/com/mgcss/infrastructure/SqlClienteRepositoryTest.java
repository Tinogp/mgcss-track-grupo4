package com.mgcss.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.persistence.ClienteEntity;

public class SqlClienteRepositoryTest {

    private SpringDataClienteRepository jpaClienteRepoMock;
    private SqlClienteRepository sqlClienteRepository; // System Under Test

    @BeforeEach
    void setUp() {
        // Inicializamos el mock de la interfaz de Spring Data
        jpaClienteRepoMock = mock(SpringDataClienteRepository.class);
        // Inyectamos el mock en nuestro repositorio adaptador
        sqlClienteRepository = new SqlClienteRepository(jpaClienteRepoMock);
    }

    @Test
    void testSave() {
        // Arrange: Preparamos un cliente de dominio (sin ID, como si fuera nuevo)
        Cliente clienteDominio = new Cliente(null, "Laura", "laura@ejemplo.com", Cliente.TipoCliente.PREMIUM);
        
        // Preparamos la entidad que simulará devolver Spring Data (ya con ID)
        ClienteEntity entidadGuardada = new ClienteEntity();
        entidadGuardada.setId(1L);
        entidadGuardada.setNombre("Laura");
        entidadGuardada.setEmail("laura@ejemplo.com");
        entidadGuardada.setTipoCliente("PREMIUM"); // Importante: en BD se guarda como String

        // Configuramos el mock
        when(jpaClienteRepoMock.save(any(ClienteEntity.class))).thenReturn(entidadGuardada);

        // Act
        Cliente resultado = sqlClienteRepository.save(clienteDominio);

        // Assert & Verify
        assertNotNull(resultado, "El cliente devuelto no debe ser nulo");
        assertEquals(1L, resultado.getId(), "El ID debe mapearse correctamente de vuelta al dominio");
        assertEquals("Laura", resultado.getNombre());
        assertEquals(Cliente.TipoCliente.PREMIUM, resultado.getTipoCliente(), "El String debe mapearse de vuelta al Enum");
        
        // Verificamos que se llamó al save subyacente
        verify(jpaClienteRepoMock).save(any(ClienteEntity.class));
    }

    @Test
    void testFindById() {
        // Arrange
        ClienteEntity entidadSimulada = new ClienteEntity();
        entidadSimulada.setId(99L);
        entidadSimulada.setNombre("Carlos");
        entidadSimulada.setEmail("carlos@ejemplo.com");
        entidadSimulada.setTipoCliente("STANDARD");

        when(jpaClienteRepoMock.findById(99L)).thenReturn(Optional.of(entidadSimulada));

        // Act
        Optional<Cliente> resultado = sqlClienteRepository.findById(99L);

        // Assert & Verify
        assertTrue(resultado.isPresent(), "Debería devolver un Optional con contenido");
        assertEquals(99L, resultado.get().getId());
        assertEquals("Carlos", resultado.get().getNombre());
        assertEquals(Cliente.TipoCliente.STANDARD, resultado.get().getTipoCliente());
        
        verify(jpaClienteRepoMock).findById(99L);
    }

    @Test
    void testFindAll() {
        // Arrange
        ClienteEntity entidad1 = new ClienteEntity();
        entidad1.setId(1L);
        entidad1.setTipoCliente("STANDARD");

        ClienteEntity entidad2 = new ClienteEntity();
        entidad2.setId(2L);
        entidad2.setTipoCliente("PREMIUM");

        List<ClienteEntity> listaSimulada = Arrays.asList(entidad1, entidad2);
        when(jpaClienteRepoMock.findAll()).thenReturn(listaSimulada);

        // Act
        List<Cliente> resultado = sqlClienteRepository.findAll();

        // Assert & Verify
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe mapear la lista completa");
        assertEquals(1L, resultado.get(0).getId(), "El primer elemento debe estar bien mapeado");
        assertEquals(Cliente.TipoCliente.PREMIUM, resultado.get(1).getTipoCliente(), "El enum del segundo elemento debe estar bien mapeado");
        
        verify(jpaClienteRepoMock).findAll();
    }

    @Test
    void testDeleteById() {
        // Arrange (No hay mucho que preparar porque es un método void)
        Long idABorrar = 5L;

        // Act
        sqlClienteRepository.deleteById(idABorrar);

        // Assert & Verify
        // Para métodos void en repositorios de infraestructura, solo verificamos que la orden pasó hacia abajo
        verify(jpaClienteRepoMock).deleteById(idABorrar);
    }
}