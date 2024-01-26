package com.iteria.Prueba.Controller;

import com.iteria.Prueba.entity.Contrato;
import com.iteria.Prueba.service.serviceDAO.IContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contratos")
public class ContratoController {

    private final IContratoService contratoService;

    @Autowired
    public ContratoController(IContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping("/nuevo")
    public Contrato crearNuevoContrato(@RequestParam Long afiliadoId, @RequestParam Long planId) {
        return contratoService.crearNuevoContrato(afiliadoId, planId);
    }

    @PutMapping("/actualizar/{id}")
    public Contrato actualizarContrato(@PathVariable Long id, @RequestBody Contrato contratoActualizado) {
        return contratoService.actualizarContrato(id, contratoActualizado);
    }

    @PutMapping("/actualizarFechaRetiro/{id}")
    public Contrato actualizarFechaRetiroYEstado(@PathVariable Long id, @RequestParam String fechaRetiro) {
        // Llamar al método del servicio
        return contratoService.actualizarFechaRetiroYEstado(id, fechaRetiro);
    }
}
