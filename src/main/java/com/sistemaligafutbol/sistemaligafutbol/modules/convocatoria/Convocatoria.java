package com.sistemaligafutbol.sistemaligafutbol.modules.convocatoria;

import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "convocatoria")
public class Convocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private int minEquipos;

    @Column(nullable = false)
    private int maxEquipos;

    @Column(nullable = false)
    private int equiposLiguilla;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private int vueltas;

    @Column(nullable = false)
    private String premio;

    @Column(nullable = false)
    private String imagenUrl; // URL de la imagen generada con Jasper

    private boolean publicada; // Estado de la convocatoria

    //Constructores

    public Convocatoria() {
    }

    public Convocatoria(Long id, Torneo torneo, LocalDate fechaInicio, LocalDate fechaFin, int minEquipos, int maxEquipos, int equiposLiguilla, String descripcion, int vueltas, String premio, String imagenUrl, boolean publicada) {
        this.id = id;
        this.torneo = torneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.minEquipos = minEquipos;
        this.maxEquipos = maxEquipos;
        this.equiposLiguilla = equiposLiguilla;
        this.descripcion = descripcion;
        this.vueltas = vueltas;
        this.premio = premio;
        this.imagenUrl = imagenUrl;
        this.publicada = publicada;
    }

    // Getters y Setters


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

    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(int minEquipos) {
        this.minEquipos = minEquipos;
    }

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public int getEquiposLiguilla() {
        return equiposLiguilla;
    }

    public void setEquiposLiguilla(int equiposLiguilla) {
        this.equiposLiguilla = equiposLiguilla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public String getPremio() {
        return premio;
    }

    public void setPremio(String premio) {
        this.premio = premio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public boolean isPublicada() {
        return publicada;
    }

    public void setPublicada(boolean publicada) {
        this.publicada = publicada;
    }
}

