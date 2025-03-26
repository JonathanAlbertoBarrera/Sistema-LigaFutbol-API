package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

// ArbitroRequest.java
public class ArbitroRequest {
    private ArbitroData arbitro;
    private String imagen;

    // Getters y Setters

    public ArbitroData getArbitro() {
        return arbitro;
    }

    public void setArbitro(ArbitroData arbitro) {
        this.arbitro = arbitro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public static class ArbitroData {
        private String email;
        private String password;
        private String nombreCompleto;

        // Getters y Setters

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

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }
    }
}