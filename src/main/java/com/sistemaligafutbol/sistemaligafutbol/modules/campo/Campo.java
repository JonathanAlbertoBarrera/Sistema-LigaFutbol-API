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
    @Column(name = "estatus_campo")
    private boolean estatusCampo;

    // Un campo tiene muchas canchas
    @OneToMany(mappedBy = "campo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Cancha> canchas;

    // Un campo puede tener muchos equipos
    @OneToMany(mappedBy = "campo")
    @JsonManagedReference
    private List<Equipo> equipos;

    //Constructores

    public Campo() {
    }

    public Campo(Long id, String nombre, String direccion, boolean estatusCampo, List<Cancha> canchas, List<Equipo> equipos) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.estatusCampo = estatusCampo;
        this.canchas = canchas;
        this.equipos = equipos;
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

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }
}
