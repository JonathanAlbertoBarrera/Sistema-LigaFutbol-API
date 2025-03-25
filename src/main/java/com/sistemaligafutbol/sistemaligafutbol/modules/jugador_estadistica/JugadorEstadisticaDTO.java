package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import jakarta.validation.constraints.NotNull;

public class JugadorEstadisticaDTO {

    @NotNull(message = "Debes ingresar el ID del jugador")
    private Long jugadorId;  // Solo el ID del jugador, no el objeto completo

    @NotNull(message = "Debes ingresar los goles que anotó el jugador")
    private int goles;

    @NotNull(message = "Debes ingresar el número de tarjetas amarillas")
    private int amarillas;

    @NotNull(message = "Debes ingresar el número de tarjetas rojas")
    private int rojas;

    private String comentarioExpulsion;

    // Getters and Setters
    public Long getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Long jugadorId) {
        this.jugadorId = jugadorId;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public int getAmarillas() {
        return amarillas;
    }

    public void setAmarillas(int amarillas) {
        this.amarillas = amarillas;
    }

    public int getRojas() {
        return rojas;
    }

    public void setRojas(int rojas) {
        this.rojas = rojas;
    }

    public String getComentarioExpulsion() {
        return comentarioExpulsion;
    }

    public void setComentarioExpulsion(String comentarioExpulsion) {
        this.comentarioExpulsion = comentarioExpulsion;
    }
}


