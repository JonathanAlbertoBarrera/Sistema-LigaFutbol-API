package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.sistemaligafutbol.sistemaligafutbol.custom_validations.anotation.ValidTorneo;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@ValidTorneo
public class TorneoDTO {

    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    private String nombreTorneo;
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    @Future(message = "La fecha de inicio debe ser mayor a la actual")
    private LocalDate fechaInicio;
    @Min(value=4, message = "El maxímo de equipos debe de ser al menos de 4 equipos")
    @Max(value = 40,message = "El máximo de equipos no puede ser mayor a 40 equipos")
    private int maxEquipos;
    @Min(value = 4, message = "El mínimo de equipos en el torneo es 4")
    private int minEquipos;
    @Min(value = 2, message = "El mínimo de equipos en liguilla es 2")
    private int equiposLiguilla;
    @Min(value = 1,message = "Mínimo debe haber 1 vuelta de partidos")
    private int vueltas;
    @NotBlank(message = "El premio no puede estar vacío")
    private String premio;

    //Getters and setters
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

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    @Min(value = 6, message = "El mínimo de equipos en el torneo es 6")
    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(@Min(value = 6, message = "El mínimo de equipos en el torneo es 6") int minEquipos) {
        this.minEquipos = minEquipos;
    }

    @Min(value = 4, message = "El mínimo de equipos en liguilla es 4")
    public int getEquiposLiguilla() {
        return equiposLiguilla;
    }

    public void setEquiposLiguilla(@Min(value = 4, message = "El mínimo de equipos en liguilla es 4") int equiposLiguilla) {
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
}
