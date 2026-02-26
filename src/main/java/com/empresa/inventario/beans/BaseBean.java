package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;

import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Data
public class BaseBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idUsuario;
    private String nombreUsuario;

    // Obtenemos los datos de sesión una sola vez
    public void cargarDatosSesion() {
        Usuario user = (Usuario) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("sessionUsuario");
        if (user != null) {
            this.idUsuario = user.getIdUsuario();
            this.nombreUsuario = user.getNombre();
        }
    }

    // Método universal de auditoría
    public void registrarAuditoria(IAuditoriaService service, Object metodo, String accion, String nivel) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFechaAuditoria(new Date());
        auditoria.setIdUsuario(idUsuario);
        auditoria.setClaseOrigen(this.getClass().getSimpleName());
        auditoria.setMetodo(metodo.toString());
        auditoria.setAccion(accion);
        auditoria.setNivel(nivel);
        service.registroAuditoria(auditoria);
    }
    
    public void registrarNavegacion(IAuditoriaService service, Object modulo, String accion) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFechaAuditoria(new Date());
        auditoria.setIdUsuario(idUsuario);
        auditoria.setClaseOrigen(this.getClass().getName());
        auditoria.setMetodo(modulo.toString());
        auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " " + accion);
        auditoria.setNivel(Mensajes.INFO.toString());
        service.registroAuditoria(auditoria);
    }

}
