package com.empresa.inventario.beans;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 


import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.model.Usuario;

import lombok.Data;

@Named("redirrecionarBean")
@ViewScoped
@Data
public class RedireecionarBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient String sessionUser = "sessionUsuario";

	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedireecionarBean.class);


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
			return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
		}
		else {
			return "/login.xhtml?faces-redirect=true";
		}
	}
	
	public void redirectIfLoggedIn() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Usuario userSession = (Usuario) context.getExternalContext().getSessionMap().get(sessionUser);

	    
	    
	    if (userSession != null) {
	        String ruta = "";
	        String rol = userSession.getRol();

	        if ("admin".equals(rol)) {
	            ruta = "/pages/admin/dashboard.xhtml";
	        } else if ("stock_manager".equals(rol)) {
	            ruta = "/pages/stock_manager/dashboard.xhtml";
	        } else if ("almacen".equals(rol)) {
	            ruta = "/pages/almacen/dashboard.xhtml";
	        }

	        if (!ruta.isEmpty()) {
	            try {
	                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + ruta);
	            } catch (java.io.IOException e) {
	                logger.error("Error en redirección automática: " + e.getMessage());
	            }
	        }
	    }
	}

}
