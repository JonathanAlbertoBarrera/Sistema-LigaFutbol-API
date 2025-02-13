package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class JugadorActualizarDTO {
    @NotBlank(message = "Debes ingresar el nombre")
    private String nombre;

    @NotBlank(message = "Debes ingresar un apellido")
    private String apellido;

    private String imagenUrl;

    @Past(message = "La fecha de nacimiento debe ser anterior a la actual")
    private LocalDate fechaNacimiento;

    //-- GETTERS Y SETTERS


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
