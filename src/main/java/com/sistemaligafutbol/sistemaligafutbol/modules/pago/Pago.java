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

}
