package com.iteria.Prueba.service.serviceDAO;

import com.iteria.Prueba.entity.Plan;

import java.util.Date;

public interface IPlanService {

    void validarFechaFinPlan(Plan plan, Date ctoFechaRegistro);
}
