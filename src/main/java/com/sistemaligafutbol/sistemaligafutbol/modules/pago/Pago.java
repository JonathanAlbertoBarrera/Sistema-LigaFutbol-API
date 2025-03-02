package com.sistemaligafutbol.sistemaligafutbol.modules.pago;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_pago")
    private String tipoPago;
    private String descripcion;
    private int monto;

    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;

    @Column(name = "fecha_limite")
    @Temporal(TemporalType.DATE)
    private Date fechaLimitePago;

    @Column(name = "estatus_pago")
    private boolean estatusPago;

    // Relación con Equipo (Un equipo puede tener muchos pagos)
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    @JsonBackReference
    private Equipo equipo;

    //Constructores

    public Pago() {
    }

    public Pago(Long id, String tipoPago, String descripcion, int monto, Date fechaPago, Date fechaLimitePago, boolean estatusPago, Equipo equipo) {
        this.id = id;
        this.tipoPago = tipoPago;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.fechaLimitePago = fechaLimitePago;
        this.estatusPago = estatusPago;
        this.equipo = equipo;
    }

    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Date getFechaLimitePago() {
        return fechaLimitePago;
    }

    public void setFechaLimitePago(Date fechaLimitePago) {
        this.fechaLimitePago = fechaLimitePago;
    }

    public boolean isEstatusPago() {
        return estatusPago;
    }

    public void setEstatusPago(boolean estatusPago) {
        this.estatusPago = estatusPago;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
