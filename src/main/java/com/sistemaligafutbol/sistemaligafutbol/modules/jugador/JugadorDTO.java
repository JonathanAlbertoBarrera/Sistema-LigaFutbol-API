package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import jakarta.validation.constraints.NotBlank;

public class JugadorDTO {
    @NotBlank
    private String nombre;
    private String apellido;
    private String imagenUrl;


    //--GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
