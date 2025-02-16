package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipo")
public class Equipo {

    public interface EquipoViews {
        interface Lista {}  // Vista solo para GET /equipos
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_equipo", nullable = false)
    private String nombreEquipo;

    private String logo;

    @Column(name = "solicitud_estatus")
    @JsonView(EquipoViews.Lista.class)
    private boolean solicitudEstatus;

    @Column(name = "inscripcion_estatus")
    @JsonView(EquipoViews.Lista.class)
    private boolean inscripcionEstatus;

    // Relación con Jugador (Un equipo tiene muchos jugadores)
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Jugador> jugadores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_campo", nullable = false)
    private Campo campo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isSolicitudEstatus() {
        return solicitudEstatus;
    }

    public void setSolicitudEstatus(boolean solicitudEstatus) {
        this.solicitudEstatus = solicitudEstatus;
    }

    public boolean isInscripcionEstatus() {
        return inscripcionEstatus;
    }

    public void setInscripcionEstatus(boolean inscripcionEstatus) {
        this.inscripcionEstatus = inscripcionEstatus;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }
}
