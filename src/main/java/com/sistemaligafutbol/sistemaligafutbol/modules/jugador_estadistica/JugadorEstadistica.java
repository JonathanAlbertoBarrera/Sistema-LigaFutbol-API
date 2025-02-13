package com.sistemaligafutbol.sistemaligafutbol.modules.jugador_estadistica;

import com.sistemaligafutbol.sistemaligafutbol.modules.jugador.Jugador;
import jakarta.persistence.*;

@Entity
@Table(name = "jugador_estadistica")
public class JugadorEstadistica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Jugador (Muchos registros de estadísticas pertenecen a un jugador)
    @ManyToOne
    @JoinColumn(name = "id_jugador", nullable = false)
    private Jugador jugador;

    private int goles;
    private int amarillas;
    private int rojas;
    @Column(name = "comentario_expulsion")
    private String comentarioExpulsion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
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
