package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica.JugadorEstadisticaDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PartidoResultadoDTO {
    @NotNull(message = "Debes ingresar los goles que anotó el equipo local")
    private int golesLocal;
    @NotNull(message = "Debes ingresar los goles que anotó el equipo visitante")
    private int golesVisitante;
    private Integer golesLocalPenales;
    private Integer golesVisitantePenales;
    private String tipoDesempate;
    private Integer autogolesLocal;
    private Integer autogolesVisitante;
    private List<JugadorEstadisticaDTO> estadisticasLocal;
    private List<JugadorEstadisticaDTO> estadisticasVisitante;

    // Getters and Setters


    @NotNull(message = "Debes ingresar los goles que anotó el equipo local")
    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(@NotNull(message = "Debes ingresar los goles que anotó el equipo local") int golesLocal) {
        this.golesLocal = golesLocal;
    }

    @NotNull(message = "Debes ingresar los goles que anotó el equipo visitante")
    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(@NotNull(message = "Debes ingresar los goles que anotó el equipo visitante") int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public Integer getGolesLocalPenales() {
        return golesLocalPenales;
    }

    public void setGolesLocalPenales(Integer golesLocalPenales) {
        this.golesLocalPenales = golesLocalPenales;
    }

    public Integer getGolesVisitantePenales() {
        return golesVisitantePenales;
    }

    public void setGolesVisitantePenales(Integer golesVisitantePenales) {
        this.golesVisitantePenales = golesVisitantePenales;
    }

    public String getTipoDesempate() {
        return tipoDesempate;
    }

    public void setTipoDesempate(String tipoDesempate) {
        this.tipoDesempate = tipoDesempate;
    }

    public Integer getAutogolesLocal() {
        return autogolesLocal;
    }

    public void setAutogolesLocal(Integer autogolesLocal) {
        this.autogolesLocal = autogolesLocal;
    }

    public Integer getAutogolesVisitante() {
        return autogolesVisitante;
    }

    public void setAutogolesVisitante(Integer autogolesVisitante) {
        this.autogolesVisitante = autogolesVisitante;
    }

    public List<JugadorEstadisticaDTO> getEstadisticasLocal() {
        return estadisticasLocal;
    }

    public void setEstadisticasLocal(List<JugadorEstadisticaDTO> estadisticasLocal) {
        this.estadisticasLocal = estadisticasLocal;
    }

    public List<JugadorEstadisticaDTO> getEstadisticasVisitante() {
        return estadisticasVisitante;
    }

    public void setEstadisticasVisitante(List<JugadorEstadisticaDTO> estadisticasVisitante) {
        this.estadisticasVisitante = estadisticasVisitante;
    }
}


