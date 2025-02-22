package com.sistemaligafutbol.sistemaligafutbol.security.auth;

public class AuthResponse {
    private String token;
    private String correo;
    private String roles;
    private Long id;

    public AuthResponse(String token, String correo, String roles, Long id) {
        this.token = token;
        this.correo = correo;
        this.roles = roles;
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

