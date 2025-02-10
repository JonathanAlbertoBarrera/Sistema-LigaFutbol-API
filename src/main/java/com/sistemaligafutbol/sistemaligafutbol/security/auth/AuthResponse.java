package com.sistemaligafutbol.sistemaligafutbol.security.auth;

public class AuthResponse {
    private String token;
    private String correo;
    private String roles;

    public AuthResponse(String token, String correo, String roles) {
        this.token = token;
        this.correo = correo;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRoles() {
        return roles;
    }
}

