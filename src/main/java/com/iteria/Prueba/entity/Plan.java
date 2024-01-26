package com.iteria.Prueba.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.List;

@Entity
@Table(name = "PLAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLN_ID")
    private Long plnId;

    @Column(name = "PLN_NOMBRE", length = 15)
    private String plnNombre;

    @Column(name = "PLN_FECHA_INICIO")
    private LocalDate plnFechaInicio;

    @Column(name = "PLN_FECHA_FIN")
    private LocalDate plnFechaFin;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Contrato> contratos;

    @Column(name = "PLN_ESTADO")
    @Enumerated(EnumType.STRING)
    private EstadoPlan plnEstado;

    public void validarFechas() {
        if (plnFechaInicio != null && plnFechaFin != null) {
            // Verificar si la fecha de inicio es posterior a la fecha de fin
            if (plnFechaInicio.isAfter(plnFechaFin)) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }

            // Verificar alguna otra condición
            if (condicionPersonalizada()) {
                throw new IllegalArgumentException("Mensaje de error personalizado.");
            }

            // Puedes agregar más lógica de validación según tus necesidades
        }
    }

    private boolean condicionPersonalizada() {
        if (plnFechaInicio != null && plnFechaFin != null) {
            long diasPermitidos = 30;
            return plnFechaInicio.until(plnFechaFin).getDays() <= diasPermitidos;
        }
        return false;
    }
}