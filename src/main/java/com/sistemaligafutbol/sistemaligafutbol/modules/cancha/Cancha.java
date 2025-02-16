package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.Partido;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cancha")
public class Cancha {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "numero_cancha")
    private int numeroCancha;
    private String descripcion;
    @Column(name = "estatus_cancha")
    private boolean estatusCancha;

    // Relación con Campo (Muchas canchas pertenecen a un solo campo)
    @ManyToOne
    @JoinColumn(name = "id_campo", nullable = false)
    @JsonBackReference
    private Campo campo;

    // Relación con Partido (Una cancha puede tener muchos partidos)
    @OneToMany(mappedBy = "cancha")
    private List<Partido> partidos;

    //Constructores

    public Cancha() {
    }

    public Cancha(Long id, int numeroCancha, String descripcion, boolean estatusCancha, Campo campo, List<Partido> partidos) {
        this.id = id;
        this.numeroCancha = numeroCancha;
        this.descripcion = descripcion;
        this.estatusCancha = estatusCancha;
        this.campo = campo;
        this.partidos = partidos;
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroCancha() {
        return numeroCancha;
    }

    public void setNumeroCancha(int numeroCancha) {
        this.numeroCancha = numeroCancha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstatusCancha() {
        return estatusCancha;
    }

    public void setEstatusCancha(boolean estatusCancha) {
        this.estatusCancha = estatusCancha;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}
