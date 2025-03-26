package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DuenoRegistroRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nombreCompleto;

    @NotBlank
    private String imagen; // Base64

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

    public @NotBlank String getImagen() {
        return imagen;
    }

    public void setImagen(@NotBlank String imagen) {
        this.imagen = imagen;
    }
}
