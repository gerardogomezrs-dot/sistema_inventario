package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;

public interface IUsuariosService {

	void save(Usuario usuario) throws ExceptionMessage;

	void update(Usuario usuario) throws Exception;

	void delete(int idUsuario, int idUsuarioSesion) throws Exception;

	List<Usuario> getAll() throws Exception;
	
	void updateProfile(Usuario usuario) throws Exception;

	Usuario getByIdUsuario(int usuario) throws Exception;

}
