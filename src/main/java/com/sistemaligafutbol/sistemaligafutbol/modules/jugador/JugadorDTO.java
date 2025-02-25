package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class JugadorDTO {
    @NotBlank(message = "Debes de ingresar el nombre completo")
    private String nombreCompleto;

    @Past(message = "La fecha de nacimiento debe ser anterior a la actual")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Debes ingresar un número de camiseta")
    private int numero_camiseta;

    @NotNull(message = "Debes asignar un equipo")
    private Long idEquipo;

    //--GETTERS Y SETTERS


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

    public @NotNull(message = "Debes asignar un equipo") Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(@NotNull(message = "Debes asignar un equipo") Long idEquipo) {
        this.idEquipo = idEquipo;
    }
}
