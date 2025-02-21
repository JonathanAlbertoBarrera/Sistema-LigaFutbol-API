package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CampoDTO {
    @NotBlank(message = "El nombre del campo no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "La latitud no puede estar vacía")
    private Double latitud;

    @NotNull(message = "La longitud no puede estar vacía")
    private Double longitud;

    public @NotBlank(message = "El nombre del campo no puede estar vacío") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre del campo no puede estar vacío") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "La dirección no puede estar vacía") String getDireccion() {
        return direccion;
    }

    public void setDireccion(@NotBlank(message = "La dirección no puede estar vacía") String direccion) {
        this.direccion = direccion;
    }

    public @NotNull(message = "La latitud no puede estar vacía") Double getLatitud() {
        return latitud;
    }

    public void setLatitud(@NotNull(message = "La latitud no puede estar vacía") Double latitud) {
        this.latitud = latitud;
    }

    public @NotNull(message = "La longitud no puede estar vacía") Double getLongitud() {
        return longitud;
    }

    public void setLongitud(@NotNull(message = "La longitud no puede estar vacía") Double longitud) {
        this.longitud = longitud;
    }
}
