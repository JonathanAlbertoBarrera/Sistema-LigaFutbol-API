package com.sistemaligafutbol.sistemaligafutbol.modules.cancha;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import jakarta.persistence.*;


@Entity
@Table(name = "cancha")
public class Cancha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cancha")
    private int numeroCancha;

    private String descripcion;

    @Column(name = "estatus_cancha")
    private boolean estatusCancha;

    @ManyToOne
    @JoinColumn(name = "id_campo", nullable = false)
    @JsonIncludeProperties({"nombre","direccion"})
    private Campo campo;

    //Constructores

    public Cancha() {
    }

    public Cancha(Long id, int numeroCancha, String descripcion, boolean estatusCancha, Campo campo) {
        this.id = id;
        this.numeroCancha = numeroCancha;
        this.descripcion = descripcion;
        this.estatusCancha = estatusCancha;
        this.campo = campo;
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
}

