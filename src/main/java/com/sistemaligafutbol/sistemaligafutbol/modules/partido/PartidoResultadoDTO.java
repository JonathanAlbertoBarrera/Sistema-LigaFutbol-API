package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import jakarta.validation.constraints.NotNull;

public class PartidoResultadoDTO {
    @NotNull(message = "Debes ingresar los goles que anotó el equipo local")
    private int golesLocal;
    @NotNull(message = "Debes ingresar los goles que anotó el equipo visitante")
    private int golesVisitante;
    private int golesLocalPenales;
    private int golesVisitantePenales;
    private String descripcionResultado;

    // Getters and Setters

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public int getGolesLocalPenales() {
        return golesLocalPenales;
    }

    public void setGolesLocalPenales(int golesLocalPenales) {
        this.golesLocalPenales = golesLocalPenales;
    }

    public int getGolesVisitantePenales() {
        return golesVisitantePenales;
    }

    public void setGolesVisitantePenales(int golesVisitantePenales) {
        this.golesVisitantePenales = golesVisitantePenales;
    }

    public String getDescripcionResultado() {
        return descripcionResultado;
    }

    public void setDescripcionResultado(String descripcionResultado) {
        this.descripcionResultado = descripcionResultado;
    }
}


