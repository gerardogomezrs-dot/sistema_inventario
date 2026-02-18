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

import lombok.Data;

@Named("perfilUsuarioBean")
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@Inject
	private IUsuariosService iUsuariosService;

	private int idUsuario;

	private String nombreUsuario;

	@Inject
	private IAuditoriaService auditoriaService;
	
	public PerfilUsuarioBean() {
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");

		try {
			int id = user.getIdUsuario();
			usuario = iUsuariosService.getByIdUsuario(id);
			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public String irADashboard() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void actualizarPerfil() throws Exception {
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		iUsuariosService.updateProfile(usuario);
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Actualizar");
		auditoria.setAccion("El usuario " + nombreUsuario + " actualizo su datos de usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}
}
