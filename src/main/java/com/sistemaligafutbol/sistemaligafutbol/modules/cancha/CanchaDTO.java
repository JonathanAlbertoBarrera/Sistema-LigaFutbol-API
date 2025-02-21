package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CanchaDTO {
    @NotNull(message = "El número de cancha es obligatorio")
    private int numeroCancha;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El ID del campo es obligatorio")
    private Long idCampo;

    //Getters and setters

    @NotNull(message = "El número de cancha es obligatorio")
    public int getNumeroCancha() {
        return numeroCancha;
    }

    public void setNumeroCancha(@NotNull(message = "El número de cancha es obligatorio") int numeroCancha) {
        this.numeroCancha = numeroCancha;
    }

    public @NotBlank(message = "La descripción no puede estar vacía") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NotBlank(message = "La descripción no puede estar vacía") String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull(message = "El ID del campo es obligatorio") Long getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(@NotNull(message = "El ID del campo es obligatorio") Long idCampo) {
        this.idCampo = idCampo;
    }
}

