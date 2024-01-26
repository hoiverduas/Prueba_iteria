package com.iteria.Prueba.Controller;

import com.iteria.Prueba.entity.Afiliado;
import com.iteria.Prueba.entity.Estado;
import com.iteria.Prueba.service.serviceDAO.IAfiliadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/afiliados")
public class AfiliadoController {

    private final IAfiliadoService afiliadoService;

    @Autowired
    public AfiliadoController(IAfiliadoService afiliadoService) {
        this.afiliadoService = afiliadoService;
    }

    @PostMapping("/registrar")
    public Afiliado registrarAfiliado(@RequestBody Afiliado afiliado) {
        return afiliadoService.registrarAfiliado(afiliado);
    }

    @PutMapping("/actualizar/{id}")
    public Afiliado actualizarAfiliado(@PathVariable Long id, @RequestBody Afiliado afiliadoActualizado) {
        return afiliadoService.actualizarAfiliado(id, afiliadoActualizado);
    }

    @PutMapping("/actualizarEstado/{afiliadoId}/{nuevoEstado}")
    public Afiliado actualizarEstadoAfiliado(@PathVariable Long afiliadoId, @PathVariable Estado nuevoEstado) {
        return afiliadoService.actualizarEstadoAfiliado(afiliadoId, nuevoEstado);
    }
}

