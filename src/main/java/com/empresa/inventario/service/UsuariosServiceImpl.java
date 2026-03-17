package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.UsuariosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;

@Named("usuarioService")
@ApplicationScoped
public class UsuariosServiceImpl implements IUsuariosService {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuariosServiceImpl.class);

	@Override
	public void save(Usuario usuario) {
		UsuariosDAO dao = new UsuariosDAO();
		String nombreUsuario = "";
		try {
			dao = new UsuariosDAO();
			nombreUsuario = dao.validarUserName(usuario.getUserName());
			dao.guardar(usuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		if (nombreUsuario.equals(usuario.getUserName())) {
			throw new ExceptionMessage("Este usuario ya existe, intenta con otro");
		}

	}

	@Override
	public void update(Usuario usuario) {
		UsuariosDAO dao = new UsuariosDAO();

		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				dao = new UsuariosDAO();
				dao.actualizar(usuario);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	@Override
	public void delete(int id, int idUsuarioSesion) {
		UsuariosDAO dao = new UsuariosDAO();

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
			logger.debug(e.getMessage());
		}
	}

	@Override
	public List<Usuario> getAll() {
		UsuariosDAO dao = new UsuariosDAO();

		List<Usuario> usuarios = new ArrayList<>();
		try {
			dao = new UsuariosDAO();
			usuarios = dao.getAll();
			if (usuarios.isEmpty()) {
				throw new ExceptionMessage("lista vacia");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return usuarios;

	}

	@Override
	public void updateProfile(Usuario usuario) {
		UsuariosDAO dao = new UsuariosDAO();
		try {
			dao = new UsuariosDAO();

			dao.actualizar(usuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	@Override
	public Usuario getByIdUsuario(int usuario) {
		UsuariosDAO dao = new UsuariosDAO();

		Usuario usuario2 = new Usuario();
		try {
			dao = new UsuariosDAO();
			usuario2 = dao.getById(usuario);
		} catch (Exception e) {
			e.getMessage();
		}
		return usuario2;
	}
}
