package com.iteria.Prueba.service.serviceImpl;

import com.iteria.Prueba.entity.*;
import com.iteria.Prueba.repository.AfiliadoRepository;
import com.iteria.Prueba.repository.TipoDocumentoRepository;
import com.iteria.Prueba.service.serviceDAO.IAfiliadoService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AfiliadoService implements IAfiliadoService {


    private final AfiliadoRepository afiliadoRepository;
    private  final TipoDocumentoRepository tipoDocumentoRepository;

    public AfiliadoService(AfiliadoRepository afiliadoRepository, TipoDocumentoRepository tipoDocumentoRepository) {
        this.afiliadoRepository = afiliadoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Override
    public Afiliado registrarAfiliado(Afiliado afiliado) {
            return afiliadoRepository.save(afiliado);
    }

    @Override
    public Afiliado actualizarAfiliado(Long id, Afiliado afiliadoActualizado) {
        Afiliado afiliadoExistente = afiliadoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Afiliado no encontrado con ID: " + id));

        // Realiza las actualizaciones necesarias en el afiliado existente
        afiliadoExistente.setAfiNombre(afiliadoActualizado.getAfiNombre());
        afiliadoExistente.setAfiApellidos(afiliadoActualizado.getAfiApellidos());
        afiliadoExistente.setTipoDocumento(afiliadoActualizado.getTipoDocumento());
        afiliadoExistente.setAfiDireccion(afiliadoActualizado.getAfiDireccion());
        afiliadoExistente.setAfiEstado(afiliadoActualizado.getAfiEstado());
        afiliadoExistente.setAfiId(afiliadoActualizado.getAfiId());
        afiliadoExistente.setAfiTelefono(afiliadoActualizado.getAfiTelefono());


        // Guarda el afiliado actualizado en la base de datos
        return afiliadoRepository.save(afiliadoExistente);
    }

    @Override
    public void validarTipoDocumento(TdcEstado tdcEstado) {
        if (tdcEstado != null) {
            // Verificar si el estado del tipo de documento es ACTIVO
            if (tdcEstado != TdcEstado.ACTIVO) {
                throw new IllegalArgumentException("No se puede utilizar un tipo de documento inactivo.");
            }
        }
    }

    @Override
    public Afiliado actualizarEstadoAfiliado(Long afiliadoId, Estado nuevoEstado) {
        Afiliado afiliadoExistente = afiliadoRepository.findById(afiliadoId)
                .orElseThrow(() -> new NoSuchElementException("Afiliado no encontrado con ID: " + afiliadoId));

        if (nuevoEstado == Estado.INACTIVO) {
            afiliadoExistente.setAfiEstado(nuevoEstado);
            Date fechaRetiro = new Date();

            // Actualizar la fecha de retiro en los contratos asociados
            actualizarFechaRetiroContratos(afiliadoExistente.getContratos(), fechaRetiro);
        } else {
            afiliadoExistente.setAfiEstado(nuevoEstado);
        }

        return afiliadoRepository.save(afiliadoExistente);
    }

    private void actualizarFechaRetiroContratos(List<Contrato> contratos, Date fechaRetiro) {
        for (Contrato contrato : contratos) {
            if (contrato.getEstadoContrato()== EstadoContrato.ACTIVO) {
                contrato.setCtoFechaRetiro(fechaRetiro);
                contrato.setEstadoContrato(EstadoContrato.INACTIVO);
            }
        }
    }



}
