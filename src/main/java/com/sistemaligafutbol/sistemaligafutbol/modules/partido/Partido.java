package com.sistemaligafutbol.sistemaligafutbol.modules.partido;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica.JugadorEstadistica;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "partido")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_torneo", nullable = false)
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "id_equipo_local", nullable = false)
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "id_equipo_visitante", nullable = false)
    private Equipo equipoVisitante;

    @ManyToOne
    @JoinColumn(name = "id_cancha", nullable = false)
    private Cancha cancha;

    @ManyToOne
    @JoinColumn(name = "id_arbitro", nullable = false)
    private Arbitro arbitro;

    @Column(name = "fecha_partido")
    private LocalDate fechaPartido;

    private LocalTime hora;

    @Column(name = "goles_local")
    private int golesLocal;
    @Column(name = "goles_visitate")
    private int golesVisitante;
    private String descripcionResultado;
    private boolean jugado;

    //Constructores


    public Partido() {
    }

    public Partido(Long id, Torneo torneo, Equipo equipoLocal, Equipo equipoVisitante, Cancha cancha, Arbitro arbitro, LocalDate fechaPartido, LocalTime hora, int golesLocal, int golesVisitante, String descripcionResultado, boolean jugado) {
        this.id = id;
        this.torneo = torneo;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.cancha = cancha;
        this.arbitro = arbitro;
        this.fechaPartido = fechaPartido;
        this.hora = hora;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.descripcionResultado = descripcionResultado;
        this.jugado = jugado;
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }

    public LocalDate getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(LocalDate fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

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

    public boolean isJugado() {
        return jugado;
    }

    public String getDescripcionResultado() {
        return descripcionResultado;
    }

    public void setDescripcionResultado(String descripcionResultado) {
        this.descripcionResultado = descripcionResultado;
    }

    public void setJugado(boolean jugado) {
        this.jugado = jugado;
    }
}
