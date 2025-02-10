package com.sistemaligafutbol.sistemaligafutbol.security.auth;

public class AuthResponse {
    private String token;
    private String username;
    private String roles;

    public AuthResponse(String token, String username, String roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRoles() {
        return roles;
    }
}

