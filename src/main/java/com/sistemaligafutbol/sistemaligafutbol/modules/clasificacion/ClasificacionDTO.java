package com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion;

public class ClasificacionDTO {
    private Long id;
    private int puntos;
    private int partidosGanados;
    private int partidosPerdidos;
    private int partidosEmpatados;
    private int golesAFavor;
    private int golesEnContra;

    private Long idEquipo;
    private String nombreEquipo;
    private String logo;

    // Constructor, getters y setters

    public ClasificacionDTO(Long id, int puntos, int partidosGanados, int partidosPerdidos, int partidosEmpatados, int golesAFavor, int golesEnContra, Long idEquipo, String nombreEquipo, String logo) {
        this.id = id;
        this.puntos = puntos;
        this.partidosGanados = partidosGanados;
        this.partidosPerdidos = partidosPerdidos;
        this.partidosEmpatados = partidosEmpatados;
        this.golesAFavor = golesAFavor;
        this.golesEnContra = golesEnContra;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.logo = logo;
    }


    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public void setPartidosGanados(int partidosGanados) {
        this.partidosGanados = partidosGanados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public void setPartidosPerdidos(int partidosPerdidos) {
        this.partidosPerdidos = partidosPerdidos;
    }

    public int getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public void setPartidosEmpatados(int partidosEmpatados) {
        this.partidosEmpatados = partidosEmpatados;
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public void setGolesAFavor(int golesAFavor) {
        this.golesAFavor = golesAFavor;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public void setGolesEnContra(int golesEnContra) {
        this.golesEnContra = golesEnContra;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

