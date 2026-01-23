package com.empresa.inventario.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.UsuariosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;

@Named("usuarioService")
@ApplicationScoped
public class UsuariosServiceImpl implements IUsuariosService {

	private UsuariosDAO dao;

	@Override
	public void save(Usuario usuario) throws Exception {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				dao = new UsuariosDAO();
				dao.guardar(usuario);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Usuario usuario) throws Exception {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				dao = new UsuariosDAO();
				dao.actualizar(usuario);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void delete(int id) throws Exception {
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
	public List<Usuario> getAll() throws Exception {
		List<Usuario> usuarios = new ArrayList<>();
		dao = new UsuariosDAO();
		usuarios = dao.getAll();
		if (usuarios.size() == 0) {
			throw new ExceptionMessage("lista vacia");
		}
		return usuarios;

	}

	@Override
	public Usuario autentificar(String user, String password) {
		Usuario usuario = new Usuario();
		if (user == null && password == null) {
			throw new ExceptionMessage("Campos vacios");
		} else {

			usuario = dao.login(user, password);
		}

		return usuario;

	}

}
