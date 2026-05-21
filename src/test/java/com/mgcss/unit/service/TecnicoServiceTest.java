package com.mgcss.unit.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.TecnicoService;

public class TecnicoServiceTest {

    private TecnicoRepository tecnicoRepository;
    private TecnicoService sut;

    @BeforeEach
    void setUp() {
        tecnicoRepository = mock(TecnicoRepository.class);
        sut = new TecnicoService(tecnicoRepository);
    }

    @Test
    void debeActivarTecnicoYGuardarCambios() {
        Tecnico tecnico = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.of(tecnico));

        boolean resultado = sut.activarTecnico(99L);

        assertTrue(resultado);
        assertTrue(tecnico.isActivo());
        verify(tecnicoRepository).save(tecnico);
    }

    @Test
    void noDebeGuardarCuandoElTecnicoYaEstaActivo() {
        Tecnico tecnico = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        tecnico.activar();
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.of(tecnico));

        boolean resultado = sut.activarTecnico(99L);

        assertFalse(resultado);
        verify(tecnicoRepository, never()).save(tecnico);
    }

    @Test
    void noDebeGuardarCuandoNoExisteElTecnico() {
        when(tecnicoRepository.findById(99L)).thenReturn(Optional.empty());

        boolean resultado = sut.activarTecnico(99L);

        assertFalse(resultado);
        verify(tecnicoRepository, never()).save(org.mockito.ArgumentMatchers.any(Tecnico.class));
    }

    @Test
    void listarTecnicosDevuelveTodosLosTecnicosDelRepositorio() {
        Tecnico tecnicoHardware = new Tecnico("Ana", Tecnico.Especialidad.HARDWARE);
        Tecnico tecnicoSoftware = new Tecnico("Luis", Tecnico.Especialidad.SOFTWARE);
        List<Tecnico> tecnicos = List.of(tecnicoHardware, tecnicoSoftware);
        when(tecnicoRepository.findAll()).thenReturn(tecnicos);

        List<Tecnico> resultado = sut.listarTecnicos();

        assertEquals(tecnicos, resultado);
        verify(tecnicoRepository).findAll();
    }
}
