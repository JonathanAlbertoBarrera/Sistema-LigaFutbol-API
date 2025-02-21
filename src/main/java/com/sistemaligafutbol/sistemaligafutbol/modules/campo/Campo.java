package com.sistemaligafutbol.sistemaligafutbol.modules.campo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.cancha.Cancha;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "campo")
public class Campo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;

    private Double latitud;
    private Double longitud;

    @Column(name = "estatus_campo")
    private boolean estatusCampo;

    @OneToMany(mappedBy = "campo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Cancha> canchas;

    //Constructores

    public Campo() {
    }

    public Campo(Long id, String nombre, String direccion, Double latitud, Double longitud, boolean estatusCampo, List<Cancha> canchas) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estatusCampo = estatusCampo;
        this.canchas = canchas;
    }

    //Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isEstatusCampo() {
        return estatusCampo;
    }

    public void setEstatusCampo(boolean estatusCampo) {
        this.estatusCampo = estatusCampo;
    }

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<Cancha> canchas) {
        this.canchas = canchas;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
