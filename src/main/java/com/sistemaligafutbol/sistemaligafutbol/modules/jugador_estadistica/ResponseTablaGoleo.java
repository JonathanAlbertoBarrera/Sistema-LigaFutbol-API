package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

public class ResponseTablaGoleo {
    private Long id;
    private String nombreCompleto;
    private String fotoJugador;
    private Long goles;
    private Long partidosJugados;
    private String logoEquipo;

    // Constructor
    public ResponseTablaGoleo(Long id, String nombreCompleto, String fotoJugador, Long goles, Long partidosJugados, String logoEquipo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.fotoJugador = fotoJugador;
        this.goles = goles;
        this.partidosJugados = partidosJugados;
        this.logoEquipo = logoEquipo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getFotoJugador() {
        return fotoJugador;
    }

    public void setFotoJugador(String fotoJugador) {
        this.fotoJugador = fotoJugador;
    }

    public Long getGoles() {
        return goles;
    }

    public void setGoles(Long goles) {
        this.goles = goles;
    }

    public Long getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(Long partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public String getLogoEquipo() {
        return logoEquipo;
    }

    public void setLogoEquipo(String logoEquipo) {
        this.logoEquipo = logoEquipo;
    }
}



