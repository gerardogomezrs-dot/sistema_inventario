package com.empresa.inventario.beans.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
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
	
	private BaseBean navegacionBean;

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
		navegacionBean.registrarAuditoria(auditoriaService, "Actualizar", 
                "El usuario " + nombreUsuario + " realizó una actualización", 
                Mensajes.INFO.toString());
		} catch (Exception e) {
			e.getMessage();
			navegacionBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, 
	                Mensajes.ERROR + ": " + e.getMessage(), 
	                Mensajes.ERROR.toString());
		}
	}
}
