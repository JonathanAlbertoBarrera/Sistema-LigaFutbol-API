package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

// Clase para la solicitud de registro móvil
public class TorneoRegistroRequest {
    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    private String nombreTorneo;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Future(message = "La fecha de inicio debe ser mayor a la actual")
    private LocalDate fechaInicio;

    @Min(value=4, message = "El máximo de equipos debe de ser al menos de 4 equipos")
    @Max(value = 40, message = "El máximo de equipos no puede ser mayor a 40 equipos")
    private int maxEquipos;

    @Min(value = 4, message = "El mínimo de equipos en el torneo es 4")
    private int minEquipos;

    @Min(value = 2, message = "El mínimo de equipos en liguilla es 2")
    private int equiposLiguilla;

    @Min(value = 1, message = "Mínimo debe haber 1 vuelta de partidos")
    private int vueltas;

    @NotBlank(message = "El premio no puede estar vacío")
    private String premio;

    @NotBlank(message = "La imagen es requerida")
    private String imagen; // Base64

    // Getters y Setters

    public @NotBlank(message = "El nombre del torneo no puede estar vacío") String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(@NotBlank(message = "El nombre del torneo no puede estar vacío") String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public @NotBlank(message = "La descripción no puede estar vacía") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank(message = "La descripción no puede estar vacía") String descripcion) {
        this.descripcion = descripcion;
    }

    public @Future(message = "La fecha de inicio debe ser mayor a la actual") LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(@Future(message = "La fecha de inicio debe ser mayor a la actual") LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Min(value = 4, message = "El máximo de equipos debe de ser al menos de 4 equipos")
    @Max(value = 40, message = "El máximo de equipos no puede ser mayor a 40 equipos")
    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(@Min(value = 4, message = "El máximo de equipos debe de ser al menos de 4 equipos") @Max(value = 40, message = "El máximo de equipos no puede ser mayor a 40 equipos") int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    @Min(value = 4, message = "El mínimo de equipos en el torneo es 4")
    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(@Min(value = 4, message = "El mínimo de equipos en el torneo es 4") int minEquipos) {
        this.minEquipos = minEquipos;
    }

    @Min(value = 2, message = "El mínimo de equipos en liguilla es 2")
    public int getEquiposLiguilla() {
        return equiposLiguilla;
    }

    public void setEquiposLiguilla(@Min(value = 2, message = "El mínimo de equipos en liguilla es 2") int equiposLiguilla) {
        this.equiposLiguilla = equiposLiguilla;
    }

    @Min(value = 1, message = "Mínimo debe haber 1 vuelta de partidos")
    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(@Min(value = 1, message = "Mínimo debe haber 1 vuelta de partidos") int vueltas) {
        this.vueltas = vueltas;
    }

    public @NotBlank(message = "El premio no puede estar vacío") String getPremio() {
        return premio;
    }

    public void setPremio(@NotBlank(message = "El premio no puede estar vacío") String premio) {
        this.premio = premio;
    }

    public @NotBlank(message = "La imagen es requerida") String getImagen() {
        return imagen;
    }

    public void setImagen(@NotBlank(message = "La imagen es requerida") String imagen) {
        this.imagen = imagen;
    }
}
