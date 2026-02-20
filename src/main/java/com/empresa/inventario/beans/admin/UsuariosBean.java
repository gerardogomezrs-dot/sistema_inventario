package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IUsuariosService;

import lombok.Data;

@Named("usuariosBean")
@javax.faces.view.ViewScoped
@Data
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IUsuariosService iUsuariosService;

	private List<Usuario> list;

	private Usuario usuario;
	
	private int idUsuario;

	public UsuariosBean() {
		usuario = new Usuario();
	}

	@PostConstruct
	public void init() {
		try {
			listaUsuarios();
			Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");
			idUsuario = user.getIdUsuario();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irANuevoUsuario() {
		return "/pages/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String irATablaUsuario() {
		return "/pages/admin/usuarios/tablaUsuarios.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void guardar() {
		try {
			iUsuariosService.save(usuario);

		} catch (ExceptionMessage e) {

			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			return;
		} catch (Exception e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
			return;
		}
	}

	public void eliminar() throws Exception {
		try {
			iUsuariosService.delete(usuario.getIdUsuario(), idUsuario);
			list = iUsuariosService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario eliminado", "El usuario fue eliminado correctamente"));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	public void actualizar() throws ExceptionMessage {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				iUsuariosService.update(usuario);
				list = iUsuariosService.getAll();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Usuario actualizado", "El usuario fue actualizado correctamente"));
			}
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	}

	public void listaUsuarios() throws Exception {
		list = iUsuariosService.getAll();
	}

	public void actualizarPerfil() throws Exception {
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		iUsuariosService.updateProfile(usuario);
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
