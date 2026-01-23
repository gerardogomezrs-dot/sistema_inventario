package com.empresa.inventario.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IUsuariosService;

import lombok.Data;

@Named("usuariosBean") // Nombre para usar en el XHTML
@ViewScoped
@Data
public class UsuariosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private IUsuariosService iUsuariosService;

	private List<Usuario> list;

	private Usuario usuario;

	private String nameUser;

	private String password;

	public UsuariosBean() {
		usuario = new Usuario();
	}

	@PostConstruct
	public void init() {
		try {
			listaUsuarios();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irANuevoUsuario() {
		return "/pages/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String irATablaUsuario() {
		return "/pages/usuarios/tablaUsuarios.xhtml?faces-redirect=true";
	}

	public String irAIndex() {
		return "/dashboard?faces-redirect=true";
	}

	public void guardar() {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				iUsuariosService.save(usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void eliminar() throws Exception {
		try {
			iUsuariosService.delete(usuario.getIdUsuario());
			list = iUsuariosService.getAll(); // refresca la tabla
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usurio eliminado", "El usuario fue eliminado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void actualizar() {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				iUsuariosService.update(usuario);
				list = iUsuariosService.getAll(); // refresca la tabla
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Usuario actualizado", "El usuario fue actualizado correctamente"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listaUsuarios() throws Exception {
		list = iUsuariosService.getAll();
	}

	public String login() throws Exception {
		String ruta = "";
		if (nameUser == null && password == null) {
			throw new Exception("Ingresa el usuario y contraseña");
		} else {
			ruta = "/dashboard?faces-redirect=true";
		}

		return ruta;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}

	public boolean esAdmin() {
		return usuario != null && "ADMIN".equals(usuario.getRol());
	}

}
