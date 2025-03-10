package com.sistemaligafutbol.sistemaligafutbol.modules.pago.tipos;

import jakarta.persistence.*;

@Entity
@Table(name = "configuracion_pago")
public class ConfiguracionPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_pago", unique = true)
    private String tipoPago; // "Arbitraje", "Cancha"

    @Column(name = "monto")
    private double monto; // Precio configurado para cada tipo de pago

    //Constructores

    public ConfiguracionPago() {
    }

    public ConfiguracionPago(Long id, String tipoPago, double monto) {
        this.id = id;
        this.tipoPago = tipoPago;
        this.monto = monto;
    }

    // Getters y Setters


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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
