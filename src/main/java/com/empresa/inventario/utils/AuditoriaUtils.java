package com.empresa.inventario.utils;

import java.util.Date;

import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.service.IAuditoriaService;

public class AuditoriaUtils {
	
	public static void registrar(IAuditoriaService service, int idUsuario, String nombreUsuario, String clase, String metodo) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFechaAuditoria(new Date());
        auditoria.setIdUsuario(idUsuario);
        auditoria.setClaseOrigen(clase);
        auditoria.setMetodo(metodo);
        auditoria.setAccion("El usuario " + nombreUsuario + " realizó una acción en " + metodo);
        auditoria.setNivel("INFO");
        service.registroAuditoria(auditoria);
    }

}
