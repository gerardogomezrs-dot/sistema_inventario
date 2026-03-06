package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.empresa.inventario.model.Usuario;

import lombok.Data;

@Named("redirrecionarBean")
@javax.faces.view.ViewScoped
@Data
public class RedireecionarBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient Usuario usuario;

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		usuario = user;
	}

	public String irAlDashboard() {
		if (usuario.getRol().equals("admin")) {
			return "/pages/admin/dashboard.xhtml?faces-redirect=true";
		} else if (usuario.getRol().equals("stock_manager")) {
			return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
		} else if (usuario.getRol().equals("almacen")) {
			return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
		}

		else {
			return "/login.xhtml?faces-redirect=true";
		}
	}

}
