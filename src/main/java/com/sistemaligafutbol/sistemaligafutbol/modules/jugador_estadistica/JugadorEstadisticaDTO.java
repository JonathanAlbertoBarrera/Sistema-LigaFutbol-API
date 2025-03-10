package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;

public class JugadorEstadisticaDTO {
    private Jugador jugador;
    private int goles;
    private int amarillas;
    private int rojas;
    private String comentarioExpulsion;

    // Getters y Setters
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public int getGoles() { return goles; }
    public void setGoles(int goles) { this.goles = goles; }

    public int getAmarillas() { return amarillas; }
    public void setAmarillas(int amarillas) { this.amarillas = amarillas; }

    public int getRojas() { return rojas; }
    public void setRojas(int rojas) { this.rojas = rojas; }

    public String getComentarioExpulsion() { return comentarioExpulsion; }
    public void setComentarioExpulsion(String comentarioExpulsion) { this.comentarioExpulsion = comentarioExpulsion; }
}

