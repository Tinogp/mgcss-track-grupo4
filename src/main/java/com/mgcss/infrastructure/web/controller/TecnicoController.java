package com.mgcss.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgcss.domain.Tecnico;
import com.mgcss.infrastructure.web.dto.TecnicoRequestDTO;
import com.mgcss.infrastructure.web.dto.TecnicoResponseDTO;
import com.mgcss.infrastructure.web.mapper.TecnicoMapper;
import com.mgcss.service.TecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tecnicos")
@Tag(name = "Técnicos", description = "Gestión de personal técnico y su estado de disponibilidad")
public class TecnicoController {

    private final TecnicoService tecnicoService;
    private final TecnicoMapper tecnicoMapper;

    public TecnicoController(TecnicoService tecnicoService, TecnicoMapper tecnicoMapper) {
        this.tecnicoService = tecnicoService;
        this.tecnicoMapper = tecnicoMapper;
    }

    @Operation(summary = "Registrar técnico", description = "Crea un nuevo técnico. Por defecto se crea en estado inactivo.")
    @PostMapping
    public ResponseEntity<TecnicoResponseDTO> crear(@RequestBody TecnicoRequestDTO dto) {
        Tecnico tecnico = tecnicoMapper.toDomain(dto);
        Tecnico guardado = tecnicoService.crearTecnico(tecnico);
        return new ResponseEntity<>(tecnicoMapper.toResponseDTO(guardado), HttpStatus.CREATED);
    }

    @Operation(summary = "Activar técnico", description = "Habilita al técnico para que pueda ser asignado a solicitudes.")
    @ApiResponse(responseCode = "200", description = "Técnico activado con éxito")
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable Long id) {
        boolean success = tecnicoService.activarTecnico(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}