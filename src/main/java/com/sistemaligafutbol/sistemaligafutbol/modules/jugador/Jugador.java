package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "jugador")
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre_completo",unique = true)
    private String nombreCompleto;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "foto_jugador")
    private String fotoJugador;

    @Column(name = "numero_camiseta")
    private int numeroCamiseta;

    @Column(name = "partidos_jugados")
    private int partidosJugados;

    private boolean habilitado;

    // Relación con Equipo (Muchos jugadores pertenecen a un equipo)
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    @JsonBackReference
    private Equipo equipo;

    //Constructores

    public Jugador() {
    }

    public Jugador(Long id, String nombreCompleto, LocalDate fechaNacimiento, String fotoJugador, int numeroCamiseta, int partidosJugados, boolean habilitado, Equipo equipo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoJugador = fotoJugador;
        this.numeroCamiseta = numeroCamiseta;
        this.partidosJugados = partidosJugados;
        this.habilitado = habilitado;
        this.equipo = equipo;
    }

    //--GETTERS Y SETTERS


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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFotoJugador() {
        return fotoJugador;
    }

    public void setFotoJugador(String fotoJugador) {
        this.fotoJugador = fotoJugador;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
