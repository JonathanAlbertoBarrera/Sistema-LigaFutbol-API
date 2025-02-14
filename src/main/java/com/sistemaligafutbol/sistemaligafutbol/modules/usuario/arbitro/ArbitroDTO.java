package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ArbitroDTO {

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "Debes ingresar una contraseña")
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreCompleto;

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

}