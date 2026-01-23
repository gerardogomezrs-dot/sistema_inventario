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
public class LoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private Usuario usuario;

	@Inject
	private IAuthService authService;

	// 1. Cambiamos void por String para controlar la navegación
	public String login() {
		try {
			usuario = new Usuario();
			usuario = authService.login(userName, password);

			if (usuario == null) {
				resetearSesion();
				añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error de Inventario", "Usuario o password inválido");
				return null; // Se queda en login.xhtml
			} else {

				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionUsuario", usuario);
				// 2. Retornamos la ruta directamente
				return "/pages/dashboard.xhtml?faces-redirect=true";
			}

		} catch (ExceptionMessage e) {
			// 3. Captura tu excepción personalizada del Service
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
		// Esto destruye la sesión actual y limpia todos los beans @SessionScoped
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		// Opcional: Si quieres que el usuario pueda reintentar de inmediato en la misma
		// página,
		// JSF creará una nueva sesión automáticamente en la siguiente petición.
	}
	
	public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

}