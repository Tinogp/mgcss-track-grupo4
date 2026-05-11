package com.mgcss.infrastructure.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgcss.domain.Solicitud;
import com.mgcss.infrastructure.web.dto.AsignarTecnicoDTO;
import com.mgcss.infrastructure.web.dto.SolicitudRequestDTO;
import com.mgcss.infrastructure.web.dto.SolicitudResponseDTO;
import com.mgcss.infrastructure.web.mapper.SolicitudMapper;
import com.mgcss.service.SolicitudService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "Operaciones para el ciclo de vida de las solicitudes de mantenimiento")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final SolicitudMapper solicitudMapper;

    public SolicitudController(SolicitudService solicitudService, SolicitudMapper solicitudMapper) {
        this.solicitudService = solicitudService;
        this.solicitudMapper = solicitudMapper;
    }

    @Operation(summary = "Crear solicitud", description = "Registra una nueva solicitud asociada a un cliente existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud(@RequestBody SolicitudRequestDTO requestDTO) {
        Solicitud solicitud = solicitudService.crearSolicitud(requestDTO.getDescripcion(), requestDTO.getClienteId());
        return new ResponseEntity<>(solicitudMapper.toResponseDTO(solicitud), HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar solicitud", description = "Obtiene los detalles completos de una solicitud por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> obtenerSolicitud(@PathVariable Long id) {
        return solicitudService.obtenerSolicitud(id)
                .map(solicitud -> ResponseEntity.ok(solicitudMapper.toResponseDTO(solicitud)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar solicitudes", description = "Devuelve todas las solicitudes registradas en el sistema.")
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listarSolicitudes() {
        List<SolicitudResponseDTO> lista = solicitudService.listarSolicitudes()
                .stream()
                .map(solicitudMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Asignar técnico", description = "Vincula un técnico a la solicitud. El técnico debe estar activo.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Técnico asignado correctamente"),
        @ApiResponse(responseCode = "400", description = "El técnico no está activo o la solicitud no permite asignación")
    })
    @PutMapping("/{id}/asignar")
    public ResponseEntity<Void> asignarTecnico(@PathVariable Long id, @RequestBody AsignarTecnicoDTO dto) {
        boolean success = solicitudService.asignarTecnico(id, dto.getTecnicoId());
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Iniciar proceso", description = "Cambia el estado a EN_PROCESO. Requiere un técnico asignado.")
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarProceso(@PathVariable Long id) {
        boolean success = solicitudService.iniciarProceso(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Cerrar solicitud", description = "Finaliza la solicitud. Solo posible si está EN_PROCESO.")
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<Void> cerrarSolicitud(@PathVariable Long id) {
        boolean success = solicitudService.cerrarSolicitud(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Reabrir solicitud", description = "Cambia una solicitud CERRADA a estado ABIERTA.")
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrirSolicitud(@PathVariable Long id) {
        boolean success = solicitudService.reabrirSolicitud(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}