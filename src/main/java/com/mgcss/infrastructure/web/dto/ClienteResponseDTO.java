package com.mgcss.infrastructure.web.dto;

import com.mgcss.domain.Cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representación del cliente en las respuestas de la API")
public class ClienteResponseDTO {

    @Schema(description = "ID único asignado al cliente", example = "10")
    private Long id;
    
    @Schema(description = "Nombre del cliente", example = "Talleres Pérez")
    private String nombre;
    
    @Schema(description = "Email registrado", example = "contacto@talleresperez.com")
    private String email;
    
    @Schema(description = "Tipo de cliente", example = "PREMIUM")
    private String tipoCliente;

    public ClienteResponseDTO() {}

    public ClienteResponseDTO(Long id, String nombre, String email, String tipoCliente) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

    public static ClienteResponseDTO fromCliente(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResponseDTO(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTipoCliente().name()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
}