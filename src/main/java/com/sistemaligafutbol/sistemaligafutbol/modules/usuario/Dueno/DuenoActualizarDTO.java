package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DuenoActualizarDTO {
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreCompleto;

    private String password;

    public DuenoActualizarDTO(String email, String nombreCompleto, String password) {
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.password = password;
    }

    // Getters y Setters

    public @NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no es válido") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "El email no puede estar vacío") @Email(message = "El email no es válido") String email) {
        this.email = email;
    }

    public @NotBlank(message = "El nombre no puede estar vacío") String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NotBlank(message = "El nombre no puede estar vacío") String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
