package com.mgcss.infrastructure.web.controller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.infrastructure.web.dto.AsignarTecnicoDTO;
import com.mgcss.infrastructure.web.dto.SolicitudRequestDTO;
import com.mgcss.infrastructure.web.mapper.SolicitudMapper;
import com.mgcss.service.SolicitudService;

@WebMvcTest(SolicitudController.class)
@Import(SolicitudMapper.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudService solicitudService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCrearSolicitud() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO("Problema con el PC", 1L);
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", Cliente.TipoCliente.STANDARD);
        Solicitud mockSolicitud = new Solicitud(1L, "Problema con el PC", cliente, null, Solicitud.Estado.ABIERTA, null, null);

        Mockito.when(solicitudService.crearSolicitud(anyString(), anyLong())).thenReturn(mockSolicitud);

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Problema con el PC"));
    }

    @Test
    void testObtenerSolicitud() throws Exception {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", Cliente.TipoCliente.STANDARD);
        Solicitud mockSolicitud = new Solicitud(1L, "Problema", cliente, null, Solicitud.Estado.ABIERTA, null, null);

        Mockito.when(solicitudService.obtenerSolicitud(1L)).thenReturn(Optional.of(mockSolicitud));

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testAsignarTecnico() throws Exception {
        AsignarTecnicoDTO dto = new AsignarTecnicoDTO(2L);

        Mockito.when(solicitudService.asignarTecnico(1L, 2L)).thenReturn(true);

        mockMvc.perform(put("/api/solicitudes/1/asignar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testReabrirSolicitud() throws Exception {
        Mockito.when(solicitudService.reabrirSolicitud(1L)).thenReturn(true);

        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerSolicitud_NotFound() throws Exception {
        // Simular que el servicio devuelve Optional vacío
        Mockito.when(solicitudService.obtenerSolicitud(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound()); // Cubre la rama del .orElse
    }

    @Test
    void testListarSolicitudes() throws Exception {
        Cliente cliente = new Cliente(1L, "Juan", "juan@test.com", Cliente.TipoCliente.STANDARD);
        Solicitud s1 = new Solicitud(1L, "Duda 1", cliente, null, Solicitud.Estado.ABIERTA, null, null);
        
        Mockito.when(solicitudService.listarSolicitudes()).thenReturn(List.of(s1));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].descripcion").value("Duda 1"));
    }

    @Test
    void testAsignarTecnico_BadRequest() throws Exception {
        AsignarTecnicoDTO dto = new AsignarTecnicoDTO(2L);
        // Simular que la asignación falla (ej: técnico no activo)
        Mockito.when(solicitudService.asignarTecnico(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(put("/api/solicitudes/1/asignar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // Cubre el retorno badRequest
    }

    @Test
    void testIniciarProceso_Success() throws Exception {
        Mockito.when(solicitudService.iniciarProceso(1L)).thenReturn(true);

        mockMvc.perform(put("/api/solicitudes/1/iniciar"))
                .andExpect(status().isOk());
    }

    @Test
    void testIniciarProceso_BadRequest() throws Exception {
        Mockito.when(solicitudService.iniciarProceso(1L)).thenReturn(false);

        mockMvc.perform(put("/api/solicitudes/1/iniciar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCerrarSolicitud_Success() throws Exception {
        Mockito.when(solicitudService.cerrarSolicitud(1L)).thenReturn(true);

        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isOk());
    }

    @Test
    void testCerrarSolicitud_BadRequest() throws Exception {
        Mockito.when(solicitudService.cerrarSolicitud(1L)).thenReturn(false);

        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testReabrirSolicitud_BadRequest() throws Exception {
        Mockito.when(solicitudService.reabrirSolicitud(1L)).thenReturn(false);

        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isBadRequest());
    }
}
