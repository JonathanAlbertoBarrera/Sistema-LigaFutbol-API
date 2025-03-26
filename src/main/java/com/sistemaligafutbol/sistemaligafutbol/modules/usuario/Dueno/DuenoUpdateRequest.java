package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DuenoUpdateRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nombreCompleto;

    private String password; // Opcional

    private String imagen; // Base64 (opcional)

    // Getters y Setters

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NotBlank String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
