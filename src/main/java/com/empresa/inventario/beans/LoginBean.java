package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuthService;

import lombok.Data;

@Named("loginBean")
@SessionScoped
@Data
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private Usuario usuario;

	@Inject
	private IAuthService authService;

	public String login() {
		try {
			usuario = new Usuario();
			usuario = authService.login(userName, password);

			if (usuario == null) {
				resetearSesion();
				añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error de Inventario", "Usuario o password inválido");
				return null; 
			} else {
				String ruta = "";

				if (usuario.getRol().equals("admin")) {

					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionUsuario",
							usuario);

					
					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

					añadirMensaje(FacesMessage.SEVERITY_INFO, "¡Bienvenido!",
							"Hola " + usuario.getNombre() + ", has iniciado sesión correctamente.");

					ruta = "/pages/admin/dashboard.xhtml?faces-redirect=true";
				}

				if (usuario.getRol().equals("almacen")) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionUsuario",
							usuario);

					FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

					añadirMensaje(FacesMessage.SEVERITY_INFO, "¡Bienvenido!",
							"Hola " + usuario.getNombre() + ", has iniciado sesión correctamente.");
					ruta = "/pages/almacen/dashboard.xhtml?faces-redirect=true";
				}

				return ruta;
			}

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			return null;
		} catch (Exception e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Ocurrió un error en el servidor. ");
			e.printStackTrace();
			return null;
		}

	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	private void resetearSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces-redirect=true";
	}
	
	public String redireccionarInicio() {
		usuario = new Usuario();
		usuario = authService.login(userName, password);
		String ruta = "";
		if(usuario.getRol().equals("admin")) {
			ruta = "/pages/admin/dashboard.xhtml?faces-redirect=true";
		}
		if(usuario.getRol().equals("almacen")) {
			ruta = "/pages/almacen/dashboard.xhtml?faces-redirect=true";
		}
		if(usuario.getRol().equals("ventas")) {
			ruta = "/pages/almacen/dashboard.xhtml?faces-redirect=true";
		}
		return ruta;
	}

}