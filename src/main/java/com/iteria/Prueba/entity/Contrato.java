package com.iteria.Prueba.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "CONTRATO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CTO_ID")
    private Long ctoId;

    @ManyToOne
    @JoinColumn(name = "AFI_ID")
    private Afiliado afiliado;

    @ManyToOne
    @JoinColumn(name = "pln_id")
    private Plan plan;

    @Column(name = "CTO_CANTIDAD_USUARIOS")
    private int ctoCantidadUsuarios;

    @Column(name = "CTO_FECHA_INICIO")
    private Date ctoFechaInicio;

    @Column(name = "CTO_FECHA_RETIRO")
    private Date ctoFechaRetiro;

    @Column(name = "CTO_FECHA_REGISTRO")
    private Date ctoFechaRegistro;

    @Column(name = "CTO_EPS", length = 20)
    private String ctoEps;

    @Enumerated(EnumType.STRING)
    @Column(name = "TDC_ESTADO")
    private EstadoContrato estadoContrato;

    public void actualizarEstado() {
        this.estadoContrato = EstadoContrato.obtenerEstado(this.ctoFechaRetiro);
    }

}
