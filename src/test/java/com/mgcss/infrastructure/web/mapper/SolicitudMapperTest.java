package com.mgcss.infrastructure.web.mapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.web.dto.SolicitudResponseDTO;

class SolicitudMapperTest {

    private SolicitudMapper solicitudMapper;

    @BeforeEach
    void setUp() {
        solicitudMapper = new SolicitudMapper();
    }

    @Test
    void testToResponseDTO_FullMapping() {
        // 1. Preparar datos de dominio completos
        Cliente cliente = new Cliente(1L, "Juan Perez", "juan@test.com", Cliente.TipoCliente.STANDARD);
        Tecnico tecnico = new Tecnico(2L, "Marta Garcia", Tecnico.Especialidad.HARDWARE, true);
        LocalDateTime fechaCreacion = LocalDateTime.now().minusDays(1);
        LocalDateTime fechaCierre = LocalDateTime.now();
        
        Solicitud solicitud = new Solicitud(
            100L, 
            "Error de sistema", 
            cliente, 
            fechaCreacion, 
            Solicitud.Estado.CERRADA, 
            tecnico, 
            fechaCierre
        );

        // 2. Ejecutar mapeo
        SolicitudResponseDTO dto = solicitudMapper.toResponseDTO(solicitud);

        // 3. Verificar todos los campos (Branch coverage de objetos anidados)
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getDescripcion()).isEqualTo("Error de sistema");
        assertThat(dto.getEstadoActual()).isEqualTo("CERRADA");
        assertThat(dto.getFechaCreacion()).isEqualTo(fechaCreacion);
        assertThat(dto.getFechaCierre()).isEqualTo(fechaCierre);
        
        // Verificación de Cliente (Cubre rama if(solicitud.getCliente() != null))
        assertThat(dto.getClienteId()).isEqualTo(1L);
        assertThat(dto.getClienteNombre()).isEqualTo("Juan Perez");
        
        // Verificación de Técnico (Cubre rama if(solicitud.getTecnicoAsignado() != null))
        assertThat(dto.getTecnicoId()).isEqualTo(2L);
        assertThat(dto.getTecnicoNombre()).isEqualTo("Marta Garcia");
    }

    @Test
    void testToResponseDTO_NullInput() {
        // Cubre la rama inicial: if (solicitud == null) return null;
        assertThat(solicitudMapper.toResponseDTO(null)).isNull();
    }

    @Test
    void testToResponseDTO_EmptyRelations() {
        // Preparar solicitud sin cliente ni técnico asignado
        Solicitud solicitud = new Solicitud(null); // Constructor que inicializa como ABIERTA
        
        // Ejecutar
        SolicitudResponseDTO dto = solicitudMapper.toResponseDTO(solicitud);

        // Verificar que no explote y que las ramas de nulos funcionen
        assertThat(dto.getClienteId()).isNull();
        assertThat(dto.getTecnicoId()).isNull();
        assertThat(dto.getEstadoActual()).isEqualTo("ABIERTA");
    }
}