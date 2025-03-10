package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

public class PartidoResultadoDTO {
    private int golesLocal;
    private int golesVisitante;
    private String descripcionResultado;

    // Getters y Setters
    public int getGolesLocal() { return golesLocal; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }

    public int getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }

    public String getDescripcionResultado() {
        return descripcionResultado;
    }

    public void setDescripcionResultado(String descripcionResultado) {
        this.descripcionResultado = descripcionResultado;
    }
}
