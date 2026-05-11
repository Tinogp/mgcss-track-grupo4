package com.mgcss.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.web.dto.TecnicoRequestDTO;
import com.mgcss.infrastructure.web.mapper.TecnicoMapper;
import com.mgcss.service.TecnicoService;

@WebMvcTest(TecnicoController.class)
@Import(TecnicoMapper.class) // Importamos el mapper real para que el controlador no falle
class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TecnicoService tecnicoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearTecnico() throws Exception {
        TecnicoRequestDTO request = new TecnicoRequestDTO();
        request.setNombre("Soporte Nivel 1");
        request.setEspecialidad(Tecnico.Especialidad.HARDWARE);

        Tecnico mockTecnico = new Tecnico(5L, "Soporte Nivel 1", Tecnico.Especialidad.HARDWARE, false);

        Mockito.when(tecnicoService.crearTecnico(any(Tecnico.class))).thenReturn(mockTecnico);

        mockMvc.perform(post("/api/tecnicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.especialidad").value("HARDWARE"));
    }

    @Test
    void testActivarTecnico() throws Exception {
        Mockito.when(tecnicoService.activarTecnico(1L)).thenReturn(true);

        mockMvc.perform(put("/api/tecnicos/1/activar"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivarTecnicoNoExistente() throws Exception {
        Mockito.when(tecnicoService.activarTecnico(99L)).thenReturn(false);

        mockMvc.perform(put("/api/tecnicos/99/activar"))
                .andExpect(status().isNotFound());
    }
}