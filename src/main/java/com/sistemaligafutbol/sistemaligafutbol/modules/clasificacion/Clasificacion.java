package com.sistemaligafutbol.sistemaligafutbol.modules.clasificacion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import jakarta.persistence.*;

@Entity
@Table(name = "clasificacion")
public class Clasificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int puntos;
    @Column(name = "partidos_ganados")
    private int partidosGanados;
    @Column(name = "partidos_perdidos")
    private int partidosPerdidos;
    @Column(name = "partidos_empatados")
    private int partidosEmpatados;
    @Column(name = "goles_favor")
    private int golesAFavor;
    @Column(name = "goles_contra")
    private int golesEnContra;

    // Relación con Torneo (Muchas clasificaciones pertenecen a un torneo)
    @ManyToOne
    @JoinColumn(name = "id_torneo", nullable = false)
    @JsonBackReference
    private Torneo torneo;

    // Relación con Equipo (Un equipo tiene solo una clasificación)
    @OneToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    //Constructores

    public Clasificacion() {
    }

    public Clasificacion(Long id, int puntos, int partidosGanados, int partidosPerdidos, int partidosEmpatados, int golesAFavor, int golesEnContra, Torneo torneo, Equipo equipo) {
        this.id = id;
        this.puntos = puntos;
        this.partidosGanados = partidosGanados;
        this.partidosPerdidos = partidosPerdidos;
        this.partidosEmpatados = partidosEmpatados;
        this.golesAFavor = golesAFavor;
        this.golesEnContra = golesEnContra;
        this.torneo = torneo;
        this.equipo = equipo;
    }

    //Getters and setters


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

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
