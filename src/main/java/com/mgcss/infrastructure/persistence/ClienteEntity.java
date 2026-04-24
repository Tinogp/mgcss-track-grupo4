package com.mgcss.infrastructure.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    
    // Guardamos el Enum como String ("STANDARD" o "PREMIUM")
    private String tipoCliente;

    // Constructor vacío obligatorio para JPA
    public ClienteEntity() {
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
}