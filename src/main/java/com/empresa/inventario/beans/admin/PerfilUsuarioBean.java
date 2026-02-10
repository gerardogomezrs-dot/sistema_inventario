package com.empresa.inventario.beans.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IUsuariosService;

import lombok.Data;

@Named("perfilUsuarioBean") 
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PerfilUsuarioBean.class);

	private Usuario usuario;

	@Inject
	private IUsuariosService iUsuariosService;

	public PerfilUsuarioBean() {

	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");

		if (user != null) {
			usuario = user;
			logger.info("LOG: Usuario recuperado de sesión: " + user.getNombre() + " "+ user.getPassword());
			String passwordDecodificado = null;
			try {
				passwordDecodificado = user.getPassword();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			usuario.setPassword(passwordDecodificado);

		} else {
			logger.info("LOG: No hay ninguna sesión activa con 'sessionUsuario'");

		}
	}

	public String irAIndex() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void actualizarPerfil() throws Exception {

		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		iUsuariosService.updateProfile(usuario);
	}

}
