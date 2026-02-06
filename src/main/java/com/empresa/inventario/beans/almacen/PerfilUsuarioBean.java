package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.model.Usuario;

import lombok.Data;

@Named("perfilUsuarioAlmacenBean") // Nombre para usar en el XHTML
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PerfilUsuarioBean.class);

	private Usuario usuario;

	public PerfilUsuarioBean() {

	}

	@PostConstruct
	public void init() {

		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");

		if (user != null) {
			usuario = user;
			logger.info("LOG: Usuario recuperado de sesión: " + user.getNombre() + " " + user.getPassword());
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
		return "/pages/almacen/dashboard?faces-redirect=true";
	}

}
