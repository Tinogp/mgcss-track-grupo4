package com.mgcss.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.persistence.ClienteEntity;
import com.mgcss.infrastructure.persistence.SolicitudEntity;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

public class SqlSolicitudRepositoryTest {

    private SpringDataSolicitudRepository jpaSolicitudRepoMock;
    private SqlSolicitudRepository sqlSolicitudRepository; // System Under Test

    @BeforeEach
    void setUp() {
        jpaSolicitudRepoMock = mock(SpringDataSolicitudRepository.class);
        sqlSolicitudRepository = new SqlSolicitudRepository(jpaSolicitudRepoMock);
    }

    @Test
    void testSave_MapeaCorrectamenteEntidadYRelaciones() {
        // 1. Arrange: Preparamos un objeto de dominio con sus relaciones
        Cliente clienteMock = mock(Cliente.class);
        when(clienteMock.getId()).thenReturn(1L);
        when(clienteMock.getNombre()).thenReturn("Juan");
        when(clienteMock.getEmail()).thenReturn("juan@ejemplo.com");
        when(clienteMock.getTipoCliente()).thenReturn(Cliente.TipoCliente.STANDARD);

        Tecnico tecnicoMock = mock(Tecnico.class);
        when(tecnicoMock.getId()).thenReturn(99L);
        when(tecnicoMock.getNombre()).thenReturn("Ana");
        when(tecnicoMock.getEspecialidad()).thenReturn(Tecnico.Especialidad.HARDWARE);
        when(tecnicoMock.isActivo()).thenReturn(true);

        Solicitud solicitudDominio = mock(Solicitud.class);
        when(solicitudDominio.getId()).thenReturn(10L);
        when(solicitudDominio.getDescripcion()).thenReturn("Pantalla rota");
        when(solicitudDominio.getEstado()).thenReturn(Solicitud.Estado.ABIERTA);
        when(solicitudDominio.getCliente()).thenReturn(clienteMock);
        when(solicitudDominio.getTecnicoAsignado()).thenReturn(tecnicoMock);

        // Preparamos lo que devolverá la base de datos (Entidad)
        SolicitudEntity entidadGuardada = new SolicitudEntity();
        entidadGuardada.setId(10L);
        entidadGuardada.setDescripcion("Pantalla rota");
        entidadGuardada.setEstadoActual("ABIERTA");
        
        ClienteEntity cEntity = new ClienteEntity();
        cEntity.setId(1L);
        cEntity.setTipoCliente("STANDARD");
        entidadGuardada.setCliente(cEntity);

        TecnicoEntity tEntity = new TecnicoEntity();
        tEntity.setId(99L);
        tEntity.setEspecialidad("HARDWARE");
        entidadGuardada.setTecnicoAsignado(tEntity);

        when(jpaSolicitudRepoMock.save(any(SolicitudEntity.class))).thenReturn(entidadGuardada);

        // 2. Act
        Solicitud resultado = sqlSolicitudRepository.save(solicitudDominio);

        // 3. Assert & Verify
        assertNotNull(resultado, "El resultado mapeado no debe ser nulo");
        assertEquals(10L, resultado.getId());
        assertEquals("Pantalla rota", resultado.getDescripcion());
        
        // Verificamos que los sub-objetos se mapearon de vuelta correctamente
        assertNotNull(resultado.getCliente(), "El cliente no debe ser nulo");
        assertEquals(1L, resultado.getCliente().getId());
        
        assertNotNull(resultado.getTecnicoAsignado(), "El técnico no debe ser nulo");
        assertEquals(99L, resultado.getTecnicoAsignado().getId());

        verify(jpaSolicitudRepoMock).save(any(SolicitudEntity.class));
    }

    @Test
    void testFindById_CuandoExiste_ReconstruyeDominioCompleto() {
        // 1. Arrange: Simulamos una entidad completa recuperada de la BD
        LocalDateTime fechaCreacion = LocalDateTime.now();
        
        SolicitudEntity entidadSimulada = new SolicitudEntity();
        entidadSimulada.setId(5L);
        entidadSimulada.setDescripcion("Fallo en red");
        entidadSimulada.setFechaCreacion(fechaCreacion);
        entidadSimulada.setEstadoActual("EN_PROCESO");

        ClienteEntity cEntity = new ClienteEntity();
        cEntity.setId(2L);
        cEntity.setNombre("Empresa S.A.");
        cEntity.setEmail("contacto@empresa.com");
        cEntity.setTipoCliente("PREMIUM");
        entidadSimulada.setCliente(cEntity);

        TecnicoEntity tEntity = new TecnicoEntity();
        tEntity.setId(8L);
        tEntity.setNombre("Carlos");
        tEntity.setEspecialidad("REDES");
        tEntity.setActivo(false);
        entidadSimulada.setTecnicoAsignado(tEntity);

        when(jpaSolicitudRepoMock.findById(5L)).thenReturn(Optional.of(entidadSimulada));

        // 2. Act
        Optional<Solicitud> resultado = sqlSolicitudRepository.findById(5L);

        // 3. Assert & Verify
        assertTrue(resultado.isPresent(), "Debería encontrar la solicitud");
        Solicitud solicitud = resultado.get();
        
        assertEquals(5L, solicitud.getId());
        assertEquals("Fallo en red", solicitud.getDescripcion());
        assertEquals(Solicitud.Estado.EN_PROCESO, solicitud.getEstado());
        assertEquals(fechaCreacion, solicitud.getFechaCreacion());

        // Verificar Cliente reconstruido
        assertNotNull(solicitud.getCliente());
        assertEquals(2L, solicitud.getCliente().getId());
        assertEquals("Empresa S.A.", solicitud.getCliente().getNombre());
        assertEquals(Cliente.TipoCliente.PREMIUM, solicitud.getCliente().getTipoCliente());

        // Verificar Tecnico reconstruido
        assertNotNull(solicitud.getTecnicoAsignado());
        assertEquals(8L, solicitud.getTecnicoAsignado().getId());
        assertEquals("Carlos", solicitud.getTecnicoAsignado().getNombre());
        assertEquals(Tecnico.Especialidad.REDES, solicitud.getTecnicoAsignado().getEspecialidad());
        assertFalse(solicitud.getTecnicoAsignado().isActivo());

        verify(jpaSolicitudRepoMock).findById(5L);
    }

    @Test
    void testFindById_CuandoNoExiste_DevuelveOptionalVacio() {
        // Arrange
        when(jpaSolicitudRepoMock.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Solicitud> resultado = sqlSolicitudRepository.findById(999L);

        // Assert & Verify
        assertFalse(resultado.isPresent(), "Debería devolver Optional.empty si no existe en BD");
        verify(jpaSolicitudRepoMock).findById(999L);
    }
}