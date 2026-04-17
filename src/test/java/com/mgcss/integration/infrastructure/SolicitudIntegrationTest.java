package com.mgcss.integration.infrastructure;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.SpringDataTecnicoRepository;
import com.mgcss.infrastructure.SqlSolicitudRepository;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@DataJpaTest // Levanta contexto parcial de persistencia y base de datos H2
@ActiveProfiles("test")
@Tag("integration") // Diferenciación formal de tests
@Import(SqlSolicitudRepository.class)
public class SolicitudIntegrationTest {

    @Autowired
    private SqlSolicitudRepository repository;

    @Autowired
    private SpringDataTecnicoRepository tecnicoRepository;

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

    @Test
    void debePersistirSolicitudConTecnicoAsignadoYCargarloCorrectamente() {
        TecnicoEntity tecnicoEntity = new TecnicoEntity();
        tecnicoEntity.setNombre("Ana");
        tecnicoEntity.setEspecialidad(Tecnico.Especialidad.HARDWARE.name());
        tecnicoEntity.setActivo(true);
        tecnicoEntity = tecnicoRepository.save(tecnicoEntity);

        Tecnico tecnico = new Tecnico(
            tecnicoEntity.getId(),
            tecnicoEntity.getNombre(),
            Tecnico.Especialidad.valueOf(tecnicoEntity.getEspecialidad()),
            tecnicoEntity.isActivo()
        );

        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion("Falla de red");
        assertTrue(solicitud.asignarTecnico(tecnico));

        Solicitud guardada = repository.save(solicitud);

        assertNotNull(guardada.getId());
        assertNotNull(guardada.getTecnicoAsignado());
        assertEquals(tecnicoEntity.getId(), guardada.getTecnicoAsignado().getId());

        Optional<Solicitud> recuperada = repository.findById(guardada.getId());
        assertTrue(recuperada.isPresent());
        assertEquals("Falla de red", recuperada.get().getDescripcion());
        assertEquals(Tecnico.Especialidad.HARDWARE, recuperada.get().getTecnicoAsignado().getEspecialidad());
        assertTrue(recuperada.get().getTecnicoAsignado().isActivo());
    }
}