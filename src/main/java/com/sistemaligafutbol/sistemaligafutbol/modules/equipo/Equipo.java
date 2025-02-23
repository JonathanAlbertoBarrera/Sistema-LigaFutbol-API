package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import jakarta.persistence.*;


@Entity
@Table(name = "equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_equipo", nullable = false)
    private String nombreEquipo;
    private String logo;

    @Column(name = "equipo_estatus")
    private boolean equipoEstatus;

    @ManyToOne
    @JoinColumn(name = "id_dueno", nullable = false)
    private Dueno dueno;

    @ManyToOne
    @JoinColumn(name = "id_campo", nullable = false)
    private Campo campo;

    //Constructores


    public Equipo() {
    }

    public Equipo(Long id, String nombreEquipo, String logo, boolean equipoEstatus, Dueno dueno, Campo campo) {
        this.id = id;
        this.nombreEquipo = nombreEquipo;
        this.logo = logo;
        this.equipoEstatus = equipoEstatus;
        this.dueno = dueno;
        this.campo = campo;
    }

    //Getters and setters

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isEquipoEstatus() {
        return equipoEstatus;
    }

    public void setEquipoEstatus(boolean equipoEstatus) {
        this.equipoEstatus = equipoEstatus;
    }

    public Dueno getDueno() {
        return dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }
}
