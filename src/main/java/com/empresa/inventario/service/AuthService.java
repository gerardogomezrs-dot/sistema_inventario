package com.empresa.inventario.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.UsuariosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;

@Named("authService")
@ApplicationScoped
public class AuthService implements IAuthService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient UsuariosDAO dao = new UsuariosDAO();
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

	

	@Override
	public Usuario login(String userName, String password) {
		Usuario usuario = new Usuario();
		if (userName == null || password == null) {
			return null;
		}
		try {
			usuario = dao.login(userName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (usuario == null) {
			throw new ExceptionMessage("No existe el usuario");
		}
		if (!usuario.isActivo()) {
			throw new ExceptionMessage("Usuario inactivo, consulta con el administrador");
		}
		String passwordDecoficado = null;
		try {
			passwordDecoficado = usuario.getPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (password.equals(passwordDecoficado)) {
			logger.info("Bienvenido");
		} else {
			throw new ExceptionMessage("Contraseña incorrecta");
		}
		return usuario;	
	}
}
