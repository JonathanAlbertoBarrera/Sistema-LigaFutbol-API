package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ArbitroRegistroDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nombreCompleto;

    private String imagenUrl;

    // Getters y Setters


    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NotBlank String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}