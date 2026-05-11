package com.mgcss.infrastructure.web.dto;

import com.mgcss.domain.Cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para el alta de un nuevo cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nombre o razón social del cliente", example = "Talleres Pérez")
    private String nombre;
    
    @Schema(description = "Correo electrónico institucional", example = "contacto@talleresperez.com")
    private String email;
    
    @Schema(description = "Nivel de contrato del cliente", example = "PREMIUM")
    private Cliente.TipoCliente tipoCliente;

    public ClienteRequestDTO() {}

    public ClienteRequestDTO(String nombre, String email, Cliente.TipoCliente tipoCliente) {
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Cliente.TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(Cliente.TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
}