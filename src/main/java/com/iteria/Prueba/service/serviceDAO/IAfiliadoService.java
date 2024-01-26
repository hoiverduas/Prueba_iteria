package com.iteria.Prueba.service.serviceDAO;

import com.iteria.Prueba.entity.*;

public interface IAfiliadoService {
   Afiliado registrarAfiliado(Afiliado afiliado);
   Afiliado actualizarAfiliado(Long id, Afiliado afiliadoActualizado);
   void validarTipoDocumento(TdcEstado tdcEstado);

   Afiliado actualizarEstadoAfiliado(Long afiliadoId, Estado nuevoEstado);
}
