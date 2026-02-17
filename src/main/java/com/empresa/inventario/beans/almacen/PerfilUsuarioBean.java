package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IUsuariosService;

import lombok.Data;

@Named("perfilUsuarioAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@Inject
	private IUsuariosService iUsuariosService;

	public PerfilUsuarioBean() {

	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario");
		try {
			int id = user.getIdUsuario();
			usuario = iUsuariosService.getByIdUsuario(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarPerfil() throws Exception {
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		iUsuariosService.updateProfile(usuario);
	}
	

	public String irAIndex() {
		return "/pages/almacen/dashboard?faces-redirect=true";
	}
}
