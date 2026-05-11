package com.mgcss.infrastructure.web.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.mgcss.domain.Tecnico;

public class TecnicoResponseDTOTest {

    @Test
    void testFromTecnico_FullData() {
        // Preparar dominio con datos completos
        Tecnico tecnico = new Tecnico(1L, "Marta García", Tecnico.Especialidad.REDES, true);

        // Ejecutar
        TecnicoResponseDTO dto = TecnicoResponseDTO.fromTecnico(tecnico);

        // Verificar mapeo
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Marta García");
        assertThat(dto.getEspecialidad()).isEqualTo("REDES");
        assertThat(dto.isActivo()).isTrue();
    }

    @Test
    void testFromTecnico_NullInput() {
        // Cubre la rama: if (tecnico == null) return null;
        assertThat(TecnicoResponseDTO.fromTecnico(null)).isNull();
    }

    @Test
    void testFromTecnico_NullEspecialidad() {
        // Preparar dominio sin especialidad para cubrir la rama ternaria del mapeo
        Tecnico tecnico = new Tecnico(2L, "Juan Sin Especialidad", null, false);

        // Ejecutar
        TecnicoResponseDTO dto = TecnicoResponseDTO.fromTecnico(tecnico);

        // Verificar que la especialidad sea null en el String del DTO
        assertThat(dto.getEspecialidad()).isNull();
        assertThat(dto.getNombre()).isEqualTo("Juan Sin Especialidad");
    }

    @Test
    void testGettersAndSetters() {
        // Test de constructor vacío y setters para cubrir métodos individuales
        TecnicoResponseDTO dto = new TecnicoResponseDTO();
        
        dto.setId(10L);
        assertThat(dto.getId()).isEqualTo(10L);
        
        dto.setNombre("Prueba");
        assertThat(dto.getNombre()).isEqualTo("Prueba");
        
        dto.setEspecialidad("SOFTWARE");
        assertThat(dto.getEspecialidad()).isEqualTo("SOFTWARE");
        
        dto.setActivo(true);
        assertThat(dto.isActivo()).isTrue();
    }

    @Test
    void testConstructorWithParameters() {
        // Cubre el constructor con argumentos
        TecnicoResponseDTO dto = new TecnicoResponseDTO(1L, "Nombre", "HARDWARE", true);
        
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Nombre");
        assertThat(dto.getEspecialidad()).isEqualTo("HARDWARE");
        assertThat(dto.isActivo()).isTrue();
    }
}