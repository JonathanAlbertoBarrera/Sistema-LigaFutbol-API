package com.sistemaligafutbol.sistemaligafutbol.security.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    @NotBlank(message = "El email esta vacío")
    @Email(message = "El email no es válido")
    private String email;
    @NotBlank(message = "La contraseña esta vacía")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


