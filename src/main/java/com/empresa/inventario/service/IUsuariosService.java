package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Usuario;

public interface IUsuariosService {

	void save(Usuario usuario) throws Exception;

	void update(Usuario usuario) throws Exception;

	void delete(int idUsuario) throws Exception;

	List<Usuario> getAll() throws Exception;
	
	void updateProfile(Usuario usuario) throws Exception;

}
