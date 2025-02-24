package com.sistemaligafutbol.sistemaligafutbol.modules.solicitud;

import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import jakarta.persistence.*;

@Entity
@Table(name = "solicitud")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipoTorneo;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_torneo", nullable = false)
    private Torneo torneo;

    @Column(nullable = false)
    private Boolean inscripcionEstatus;

    @Column(nullable = false)
    private Boolean resolucion;

    @Column(nullable = false)
    private Boolean pendiente;

    //Constructores

    public Solicitud() {
    }

    public Solicitud(Long idEquipoTorneo, Equipo equipo, Torneo torneo, Boolean inscripcionEstatus, Boolean resolucion, Boolean pendiente) {
        this.idEquipoTorneo = idEquipoTorneo;
        this.equipo = equipo;
        this.torneo = torneo;
        this.inscripcionEstatus = inscripcionEstatus;
        this.resolucion = resolucion;
        this.pendiente = pendiente;
    }

    //Getters and setters

    public Long getIdEquipoTorneo() {
        return idEquipoTorneo;
    }

    public void setIdEquipoTorneo(Long idEquipoTorneo) {
        this.idEquipoTorneo = idEquipoTorneo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Boolean getInscripcionEstatus() {
        return inscripcionEstatus;
    }

    public void setInscripcionEstatus(Boolean inscripcionEstatus) {
        this.inscripcionEstatus = inscripcionEstatus;
    }

    public Boolean getResolucion() {
        return resolucion;
    }

    public void setResolucion(Boolean resolucion) {
        this.resolucion = resolucion;
    }

    public Boolean getPendiente() {
        return pendiente;
    }

    public void setPendiente(Boolean pendiente) {
        this.pendiente = pendiente;
    }
}

