package com.mgcss.infrastructure.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgcss.domain.Cliente;
import com.mgcss.infrastructure.web.dto.ClienteRequestDTO;
import com.mgcss.infrastructure.web.dto.ClienteResponseDTO;
import com.mgcss.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para gestión de Clientes.
 * Expone operaciones de CRUD para clientes del sistema.
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes del sistema")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Crear un nuevo cliente.
     *
     * @param requestDTO Datos del cliente a crear
     * @return ClienteResponseDTO con el cliente creado
     */
    @PostMapping
    @Operation(
        summary = "Crear un nuevo cliente",
        description = "Crea un nuevo cliente en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Cliente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos en la solicitud"
        )
    })
    public ResponseEntity<ClienteResponseDTO> crearCliente(
            @RequestBody ClienteRequestDTO requestDTO) {
        
        Cliente clienteCreado = clienteService.crearCliente(
            requestDTO.getNombre(),
            requestDTO.getEmail(),
            requestDTO.getTipoCliente()
        );

        ClienteResponseDTO responseDTO = ClienteResponseDTO.fromCliente(clienteCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
