package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Usuario;

public interface IUsuariosService {

	void save(Usuario usuario);

	void update(Usuario usuario) ;

	void delete(int idUsuario, int idUsuarioSesion) ;

	List<Usuario> getAll() ;
	
	void updateProfile(Usuario usuario) ;

	Usuario getByIdUsuario(int usuario);

}
