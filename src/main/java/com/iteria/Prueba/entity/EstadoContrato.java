package com.iteria.Prueba.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public enum EstadoContrato {

    ACTIVO,
    INACTIVO;

    public static EstadoContrato obtenerEstado(Date fechaRetiro) {
        if (fechaRetiro == null || fechaRetiro.after(new Date())) {
            return ACTIVO;
        } else {
            return INACTIVO;
        }
    }
}
