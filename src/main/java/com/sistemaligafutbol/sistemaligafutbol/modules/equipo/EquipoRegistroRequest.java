package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipoRegistroRequest {
    @NotBlank(message = "El nombre del equipo no puede estar vacío")
    private String nombreEquipo;

    @NotNull(message = "El id del usuario es necesario")
    private Long idUsuario;

    @NotNull(message = "Elegir el campo a jugar es necesario")
    private Long idCampo;

    @NotBlank(message = "La imagen es requerida")
    private String imagen; // Base64

    // Getters y Setters

    public @NotBlank(message = "El nombre del equipo no puede estar vacío") String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(@NotBlank(message = "El nombre del equipo no puede estar vacío") String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public @NotNull(message = "El id del usuario es necesario") Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(@NotNull(message = "El id del usuario es necesario") Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public @NotNull(message = "Elegir el campo a jugar es necesario") Long getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(@NotNull(message = "Elegir el campo a jugar es necesario") Long idCampo) {
        this.idCampo = idCampo;
    }

    public @NotBlank(message = "La imagen es requerida") String getImagen() {
        return imagen;
    }

    public void setImagen(@NotBlank(message = "La imagen es requerida") String imagen) {
        this.imagen = imagen;
    }
}
