package com.mgcss.infrastructure;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.persistence.TecnicoEntity;

@DataJpaTest // Levanta una base de datos H2 en memoria y configura Spring Data JPA
public class SqlTecnicoRepositoryTest {

    @Autowired
    private SpringDataTecnicoRepository jpaRepository;

    private SqlTecnicoRepository sqlTecnicoRepository;

    @BeforeEach
    void setUp() {
        // Inicializamos el repositorio de infraestructura con el repositorio JPA real
        sqlTecnicoRepository = new SqlTecnicoRepository(jpaRepository);
    }

    @Test
    void testSave() {
        // 1. Preparar el objeto de dominio
        Tecnico tecnicoDominio = new Tecnico("Carlos Soporte", Tecnico.Especialidad.HARDWARE);
        tecnicoDominio.activar();

        // 2. Ejecutar la acción
        Tecnico guardado = sqlTecnicoRepository.save(tecnicoDominio);

        // 3. Verificar (Mapeo Dominio -> Entidad -> Dominio)
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Carlos Soporte");
        assertThat(guardado.getEspecialidad()).isEqualTo(Tecnico.Especialidad.HARDWARE);
        assertThat(guardado.isActivo()).isTrue();
    }

    @Test
    void testFindById_Found() {
        // 1. Persistir una entidad directamente para la prueba
        TecnicoEntity entity = new TecnicoEntity();
        entity.setNombre("Ana Redes");
        entity.setEspecialidad("REDES");
        entity.setActivo(false);
        entity = jpaRepository.save(entity);

        // 2. Buscar a través del repositorio de infraestructura
        Optional<Tecnico> resultado = sqlTecnicoRepository.findById(entity.getId());

        // 3. Verificar el mapeo correcto a Dominio
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Ana Redes");
        assertThat(resultado.get().getEspecialidad()).isEqualTo(Tecnico.Especialidad.REDES);
        assertThat(resultado.get().isActivo()).isFalse();
    }

    @Test
    void testFindById_NotFound() {
        // Buscar un ID que no existe
        Optional<Tecnico> resultado = sqlTecnicoRepository.findById(999L);
        assertThat(resultado).isEmpty();
    }
}