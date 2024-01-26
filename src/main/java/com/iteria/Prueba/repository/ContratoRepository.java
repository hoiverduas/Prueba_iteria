package com.iteria.Prueba.repository;

import com.iteria.Prueba.entity.Afiliado;
import com.iteria.Prueba.entity.Contrato;
import com.iteria.Prueba.entity.EstadoContrato;
import com.iteria.Prueba.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    boolean existsByAfiliadoAndPlanAndEstadoContrato(Afiliado afiliado, Plan plan, EstadoContrato estadoContrato);
}
