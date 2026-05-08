package com.mgcss.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mgcss.domain.Solicitud;
import com.mgcss.infrastructure.persistence.RegisterSolicitudEntity;

@ExtendWith(MockitoExtension.class)
class SqlRegisterSolicitudRepositoryTest {

    @Mock
    private SpringDataRegisterSolicitudRepository jpaRepository;

    @InjectMocks
    private SqlRegisterSolicitudRepository repository;

    private RegisterSolicitudEntity registerSolicitudEntity;

    @BeforeEach
    void setUp() {
        registerSolicitudEntity = new RegisterSolicitudEntity(
            1L,
            Solicitud.Estado.ABIERTA,
            Solicitud.Estado.EN_PROCESO
        );
    }

    @Test
    void testFindById() {
        Long id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.of(registerSolicitudEntity));

        Optional<RegisterSolicitudEntity> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(registerSolicitudEntity.getSolicitudId(), result.get().getSolicitudId());
        assertEquals(Solicitud.Estado.ABIERTA, result.get().getEstadoAnterior());
        assertEquals(Solicitud.Estado.EN_PROCESO, result.get().getEstadoActual());
        verify(jpaRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        Long id = 999L;
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RegisterSolicitudEntity> result = repository.findById(id);

        assertFalse(result.isPresent());
        verify(jpaRepository, times(1)).findById(id);
    }

    @Test
    void testSave() {
        when(jpaRepository.save(registerSolicitudEntity)).thenReturn(registerSolicitudEntity);

        RegisterSolicitudEntity result = repository.save(registerSolicitudEntity);

        assertNotNull(result);
        assertEquals(registerSolicitudEntity.getSolicitudId(), result.getSolicitudId());
        assertEquals(Solicitud.Estado.ABIERTA, result.getEstadoAnterior());
        assertEquals(Solicitud.Estado.EN_PROCESO, result.getEstadoActual());
        verify(jpaRepository, times(1)).save(registerSolicitudEntity);
    }

    @Test
    void testSaveMultipleEntities() {
        RegisterSolicitudEntity entity1 = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        RegisterSolicitudEntity entity2 = new RegisterSolicitudEntity(2L, Solicitud.Estado.EN_PROCESO, Solicitud.Estado.CERRADA);

        when(jpaRepository.save(entity1)).thenReturn(entity1);
        when(jpaRepository.save(entity2)).thenReturn(entity2);

        RegisterSolicitudEntity result1 = repository.save(entity1);
        RegisterSolicitudEntity result2 = repository.save(entity2);

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1L, result1.getSolicitudId());
        assertEquals(2L, result2.getSolicitudId());
        verify(jpaRepository, times(2)).save(any(RegisterSolicitudEntity.class));
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        doNothing().when(jpaRepository).deleteById(id);

        repository.deleteById(id);

        verify(jpaRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteByIdMultipleTimes() {
        Long id1 = 1L;
        Long id2 = 2L;
        doNothing().when(jpaRepository).deleteById(anyLong());

        repository.deleteById(id1);
        repository.deleteById(id2);

        verify(jpaRepository, times(1)).deleteById(id1);
        verify(jpaRepository, times(1)).deleteById(id2);
        verify(jpaRepository, times(2)).deleteById(anyLong());
    }

    @Test
    void testFindAll() {
        RegisterSolicitudEntity entity1 = new RegisterSolicitudEntity(1L, Solicitud.Estado.ABIERTA, Solicitud.Estado.EN_PROCESO);
        RegisterSolicitudEntity entity2 = new RegisterSolicitudEntity(2L, Solicitud.Estado.EN_PROCESO, Solicitud.Estado.CERRADA);
        RegisterSolicitudEntity entity3 = new RegisterSolicitudEntity(3L, Solicitud.Estado.ABIERTA, Solicitud.Estado.CERRADA);

        List<RegisterSolicitudEntity> entities = Arrays.asList(entity1, entity2, entity3);
        when(jpaRepository.findAll()).thenReturn(entities);

        List<RegisterSolicitudEntity> result = repository.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getSolicitudId());
        assertEquals(2L, result.get(1).getSolicitudId());
        assertEquals(3L, result.get(2).getSolicitudId());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList());

        List<RegisterSolicitudEntity> result = repository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void testFindAllSingleElement() {
        List<RegisterSolicitudEntity> entities = Arrays.asList(registerSolicitudEntity);
        when(jpaRepository.findAll()).thenReturn(entities);

        List<RegisterSolicitudEntity> result = repository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(registerSolicitudEntity.getSolicitudId(), result.get(0).getSolicitudId());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void testRepositoryDelegatesCorrectly() {
        Long id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.of(registerSolicitudEntity));
        when(jpaRepository.save(registerSolicitudEntity)).thenReturn(registerSolicitudEntity);

        Optional<RegisterSolicitudEntity> found = repository.findById(id);
        RegisterSolicitudEntity saved = repository.save(registerSolicitudEntity);

        assertTrue(found.isPresent());
        assertNotNull(saved);
        verify(jpaRepository, times(1)).findById(id);
        verify(jpaRepository, times(1)).save(registerSolicitudEntity);
    }
}
