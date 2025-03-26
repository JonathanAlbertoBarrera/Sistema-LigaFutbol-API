package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class JugadorUpdateRequest {
    @NotBlank(message = "Debes de ingresar el nombre completo")
    private String nombreCompleto;

    @Past(message = "La fecha de nacimiento debe ser anterior a la actual")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debes ingresar un número de camiseta")
    private int numero_camiseta;

    private String imagen; // Base64 (opcional)

    // Getters y Setters

    public @NotBlank(message = "Debes de ingresar el nombre completo") String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(@NotBlank(message = "Debes de ingresar el nombre completo") String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public @Past(message = "La fecha de nacimiento debe ser anterior a la actual") LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(@Past(message = "La fecha de nacimiento debe ser anterior a la actual") LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @NotNull(message = "Debes ingresar un número de camiseta")
    public int getNumero_camiseta() {
        return numero_camiseta;
    }

    public void setNumero_camiseta(@NotNull(message = "Debes ingresar un número de camiseta") int numero_camiseta) {
        this.numero_camiseta = numero_camiseta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
