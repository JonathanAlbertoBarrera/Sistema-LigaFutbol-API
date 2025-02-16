package com.sistemaligafutbol.sistemaligafutbol.modules.torneo;

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

    private int maxEquipos;
    private int minEquipos;

    @Column(name = "equipos_liguilla")
    private int equiposLiguilla;

    private String logoTorneo;
    private boolean estatusTorneo;
    private String motivoFinalizacion;

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partido> partidos;

    //Constructor


    public Torneo() {
    }

    public Torneo(Long id, String nombreTorneo, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, int maxEquipos, int minEquipos, int equiposLiguilla, String logoTorneo, boolean estatusTorneo, String motivoFinalizacion, List<Partido> partidos) {
        this.id = id;
        this.nombreTorneo = nombreTorneo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.maxEquipos = maxEquipos;
        this.minEquipos = minEquipos;
        this.equiposLiguilla = equiposLiguilla;
        this.logoTorneo = logoTorneo;
        this.estatusTorneo = estatusTorneo;
        this.motivoFinalizacion = motivoFinalizacion;
        this.partidos = partidos;
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

    public String getMotivoFinalizacion() {
        return motivoFinalizacion;
    }

    public void setMotivoFinalizacion(String motivoFinalizacion) {
        this.motivoFinalizacion = motivoFinalizacion;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}
