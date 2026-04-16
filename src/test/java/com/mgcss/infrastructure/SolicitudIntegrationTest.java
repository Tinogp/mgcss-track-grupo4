package com.mgcss.infrastructure;

import com.mgcss.domain.Solicitud;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Levanta contexto parcial de persistencia y base de datos H2 [cite: 475]
@Tag("integration") // Diferenciación formal de tests [cite: 494, 514]
@Import(SqlSolicitudRepository.class)
public class SolicitudIntegrationTest {

    @Autowired
    private SqlSolicitudRepository repository;

    @Test
    void debePersistirYRecuperarSolicitudCorrectamente() {
        // Arrange
        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion("Fallo en el sistema de red");

        // Act
        Solicitud guardada = repository.save(solicitud);

        // Assert
        assertNotNull(guardada.getId());
        Optional<Solicitud> recuperada = repository.findById(guardada.getId());
        
        assertTrue(recuperada.isPresent());
        assertEquals("Fallo en el sistema de red", recuperada.get().getDescripcion());
        assertEquals(Solicitud.Estado.ABIERTA, recuperada.get().getEstado());
    }
}