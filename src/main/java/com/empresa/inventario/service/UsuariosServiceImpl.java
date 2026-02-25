package com.empresa.inventario.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.UsuariosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;

@Named("usuarioService")
@ApplicationScoped
public class UsuariosServiceImpl implements IUsuariosService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient UsuariosDAO dao = new UsuariosDAO();

	@Override
	public void save(Usuario usuario) {

		String nombreUsuario = "";

		try {
			dao = new UsuariosDAO();
			nombreUsuario = dao.validarUserName(usuario.getUserName());
			dao.guardar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nombreUsuario.equals(usuario.getUserName())) {
			throw new ExceptionMessage("Este usuario ya existe, intenta con otro");
		}

	}

	@Override
	public void update(Usuario usuario) {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				dao = new UsuariosDAO();
				dao.actualizar(usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id, int idUsuarioSesion) {
		if (id == idUsuarioSesion) {
			throw new ExceptionMessage("No puedes eliminar tus datos");
		}
		try {
			if (id == 0) {
				throw new ExceptionMessage("Ingresa el id");
			} else {
				dao.eliminarUsuario(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Usuario> getAll() {
		List<Usuario> usuarios = new ArrayList<>();
		try {
			dao = new UsuariosDAO();
			usuarios = dao.getAll();
			if (usuarios.size() == 0) {
				throw new ExceptionMessage("lista vacia");
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return usuarios;

	}

	@Override
	public void updateProfile(Usuario usuario) {

		try {
			dao = new UsuariosDAO();

			dao.actualizar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Usuario getByIdUsuario(int usuario)  {
		Usuario usuario2 = new Usuario();
		try {
			dao = new UsuariosDAO();
			usuario2 = dao.getById(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario2;
	}
}
