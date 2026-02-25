package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IUsuariosService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("perfilUsuarioBean")
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private IUsuariosService iUsuariosService;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;
	
	@Inject
	public PerfilUsuarioBean(IUsuariosService iUsuariosService, IAuditoriaService auditoriaService) {
		this.iUsuariosService = iUsuariosService;
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init()  {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		
			int id = user.getIdUsuario();
			usuario = iUsuariosService.getByIdUsuario(id);
			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();
		

	}

	public String irADashboard() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void actualizarPerfil()  {
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		try {
		iUsuariosService.updateProfile(usuario);
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Actualizar");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo una actualización");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.printStackTrace();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR)+ e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}
}
