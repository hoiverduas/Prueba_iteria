package com.iteria.Prueba.service.serviceImpl;

import com.iteria.Prueba.entity.*;
import com.iteria.Prueba.exception.ContratoInvalidoException;
import com.iteria.Prueba.repository.AfiliadoRepository;
import com.iteria.Prueba.repository.ContratoRepository;
import com.iteria.Prueba.repository.PlanRepository;
import com.iteria.Prueba.service.serviceDAO.IContratoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ContratoService implements IContratoService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private AfiliadoRepository afiliadoRepository;

    @Autowired
    private PlanRepository planRepository;


    @Override
    public Contrato crearNuevoContrato(Long afiliadoId, Long planId) {




        // Obtener el afiliado y el plan
        Afiliado afiliado = afiliadoRepository.findById(afiliadoId)
                .orElseThrow(() -> new NoSuchElementException("Afiliado no encontrado con ID: " + afiliadoId));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("Plan no encontrado con ID: " + planId));

        // Verificar si el afiliado ya tiene un contrato con el mismo plan activo
        if (afiliadoYaTienePlanActivo(afiliado, plan)) {
            throw new ContratoInvalidoException("El afiliado ya tiene este plan activo.");
        }


        // Validar la regla de negocio: No se puede registrar un contrato si su plan tiene como fecha fin el mismo día de registro del contrato
        validarFechaFinPlan(plan, LocalDate.now());

        // Crear el nuevo contrato
        Contrato nuevoContrato = new Contrato();
        nuevoContrato.setAfiliado(afiliado);
        nuevoContrato.setPlan(plan);
        // Configurar otras propiedades del contrato si es necesario...

        // Guardar el nuevo contrato en la base de datos
        return contratoRepository.save(nuevoContrato);


    }

    @Override
    public Contrato actualizarContrato(Long contratoId, Contrato contratoActualizado) {
        // Verificar si el contrato existente está en la base de datos
        Contrato contratoExistente = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new NoSuchElementException("Contrato no encontrado con ID: " + contratoId));

        // Realizar actualizaciones necesarias en el contrato existente
        contratoExistente.setPlan(contratoActualizado.getPlan());
        contratoExistente.setCtoFechaInicio(contratoActualizado.getCtoFechaInicio());
        Afiliado afiliadoActualizado = contratoActualizado.getAfiliado();
        contratoExistente.setAfiliado(afiliadoActualizado);


        // Obtener el plan actualizado (asumiendo que el plan está presente en contratoActualizado)
        Plan planActualizado = contratoActualizado.getPlan();

        // Validar el estado del plan
        validarEstadoPlan(planActualizado);


        // Actualizar el plan en el contrato existente
        contratoExistente.setPlan(planActualizado);
        // Guardar el contrato actualizado en la base de datos
        return contratoRepository.save(contratoExistente);
    }



    private void validarFechaFinPlan(Plan plan, LocalDate ctoFechaRegistro) {
        if (plan.getPlnFechaFin() != null && ctoFechaRegistro != null &&
                plan.getPlnFechaFin().isEqual(ctoFechaRegistro)) {
            throw new ContratoInvalidoException("No se puede registrar un contrato con un plan que tiene fecha fin el mismo día.");
        }
    }


    private boolean afiliadoYaTienePlanActivo(Afiliado afiliado, Plan plan) {
        // Verificar si existe un contrato activo para el afiliado y el plan proporcionados
        return contratoRepository.existsByAfiliadoAndPlanAndEstadoContrato(afiliado, plan, EstadoContrato.ACTIVO);
    }

    private void validarEstadoPlan(Plan plan) {
        if (plan != null) {
            // No necesitas buscar el estado del plan en la base de datos porque son valores constantes
            if (plan.getPlnEstado() != EstadoPlan.ACTIVO) {
                throw new IllegalArgumentException("No se puede utilizar un plan inactivo.");
            }
        }
    }


    @Override
    public Contrato actualizarFechaRetiroYEstado(Long contratoId, String fechaRetiro) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new NoSuchElementException("Contrato no encontrado con ID: " + contratoId));

        // Convierte la cadena de fechaRetiro a un objeto Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaRetiroDate;
        try {
            fechaRetiroDate = dateFormat.parse(fechaRetiro);
        } catch (ParseException e) {
            // Manejar la excepción de análisis de fecha si es necesario
            throw new IllegalArgumentException("Formato de fecha no válido: " + fechaRetiro, e);
        }

        // Luego, actualiza la fecha de retiro y el estado
        contrato.setCtoFechaRetiro(fechaRetiroDate);
        contrato.actualizarEstado();

        // Guardar el contrato actualizado en la base de datos
        return contratoRepository.save(contrato);
    }

// Otros métodos del servicio...

    public LocalDate obtenerFechaActual() {
        return LocalDate.now();
    }

    private int obtenerNumeroDiasHabilesPor(int tipoUsuario) {
        Map<Integer, Integer> mapaDiasHabiles = new HashMap<>();
        mapaDiasHabiles.put(1, 10);
        mapaDiasHabiles.put(2, 8);
        mapaDiasHabiles.put(3, 7);
        return mapaDiasHabiles.getOrDefault(tipoUsuario, 0);
    }

    private LocalDate calcularFechaDevolucion(LocalDate fechaActual, int numeroDiasHabiles){
        int contador = 0;
        LocalDate fechaDevolucion = fechaActual;
        while ( contador < numeroDiasHabiles){
            fechaDevolucion = fechaDevolucion.plusDays(1);
            DayOfWeek diaSemana = fechaDevolucion.getDayOfWeek();
            if (diaSemana != DayOfWeek.SATURDAY && diaSemana != DayOfWeek.SUNDAY){
                ++contador;
            }
        }
        return fechaDevolucion;
    }

    @Transactional
    public void updateColumnType() {
        String sql = "ALTER TABLE contrato ALTER COLUMN pln_id TYPE bigint USING pln_id::bigint";
        Query query = entityManager.createNativeQuery(sql);
        query.executeUpdate();
    }

}
