package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "torneo")
public class Torneo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_torneo")
    private String nombreTorneo;

    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    @Column(name = "max_equipos")
    private int maxEquipos;
    @Column(name = "min_equipos")
    private int minEquipos;
    @Column(name = "equipos_liguilla")
    private int equiposLiguilla;
    private int vueltas;
    @Column(name = "logo_torneo")
    private String logoTorneo;
    private String premio;
    @Column(name = "estatus_llenado")
    private boolean estatusLlenado;
    private boolean iniciado;
    @Column(name = "estatus_torneo")
    private boolean estatusTorneo;
    @OneToOne
    @JoinColumn(name = "ganador_id", referencedColumnName = "id")
    private Equipo ganador;
    @Column(name = "motivo_finalizacion")
    private String motivoFinalizacion;

    //Constructor

    public Torneo() {
    }

    public Torneo(Long id, String nombreTorneo, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int maxEquipos, int minEquipos, int equiposLiguilla, int vueltas, String logoTorneo, String premio, boolean estatusLlenado, boolean iniciado, boolean estatusTorneo, Equipo ganador, String motivoFinalizacion) {
        this.id = id;
        this.nombreTorneo = nombreTorneo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxEquipos = maxEquipos;
        this.minEquipos = minEquipos;
        this.equiposLiguilla = equiposLiguilla;
        this.vueltas = vueltas;
        this.logoTorneo = logoTorneo;
        this.premio = premio;
        this.estatusLlenado = estatusLlenado;
        this.iniciado = iniciado;
        this.estatusTorneo = estatusTorneo;
        this.ganador = ganador;
        this.motivoFinalizacion = motivoFinalizacion;
    }

    //Getter and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(int minEquipos) {
        this.minEquipos = minEquipos;
    }

    public int getEquiposLiguilla() {
        return equiposLiguilla;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public void setEquiposLiguilla(int equiposLiguilla) {
        this.equiposLiguilla = equiposLiguilla;
    }

    public String getLogoTorneo() {
        return logoTorneo;
    }

    public void setLogoTorneo(String logoTorneo) {
        this.logoTorneo = logoTorneo;
    }

    public boolean isEstatusTorneo() {
        return estatusTorneo;
    }

    public void setEstatusTorneo(boolean estatusTorneo) {
        this.estatusTorneo = estatusTorneo;
    }

    public boolean isEstatusLlenado() {
        return estatusLlenado;
    }

    public void setEstatusLlenado(boolean estatusLlenado) {
        this.estatusLlenado = estatusLlenado;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public String getMotivoFinalizacion() {
        return motivoFinalizacion;
    }

    public void setMotivoFinalizacion(String motivoFinalizacion) {
        this.motivoFinalizacion = motivoFinalizacion;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }
}
