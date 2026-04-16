package com.mgcss.domain;

public class Tecnico {

    public Tecnico(Long id, String nombre, Especialidad valueOf, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = valueOf;
        this.activo = activo;
    }

    public enum Especialidad {HARDWARE, SOFTWARE, REDES};

    private Long id;
    private String nombre;
    private Especialidad especialidad;
    private boolean activo;

    public Tecnico(String nombre, Especialidad especialidad) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean activar(){
        if (!activo) {
            this.activo = true;
            return true;
        }
        return false;
    }

    public boolean desactivar(){
        if (activo) {
            this.activo = false;
            return true;
        }
        return false;
    }
    

}
