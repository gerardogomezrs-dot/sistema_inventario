package com.empresa.inventario.beans;

import java.io.Serializable;
import java.sql.SQLException;
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

@Named("usuariosBean") // Nombre para usar en el XHTML
@javax.faces.view.ViewScoped

@Data
public class UsuariosBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private transient IUsuariosService iUsuariosService;

	private List<Usuario> list;

	private Usuario usuario;


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
		return "/pages/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String irATablaUsuario() {
		return "/pages/admin/usuarios/tablaUsuarios.xhtml?faces-redirect=true";
	}

	public String irAIndex() {
		return "/pages/admin/dashboard?faces-redirect=true";
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


}
