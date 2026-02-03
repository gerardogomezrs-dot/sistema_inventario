package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Usuario;

public class UsuariosMapper {


	public Usuario mapRow(ResultSet rs) throws Exception{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("user_name"));
		String passwordDecodificado = rs.getString("password");
		String password = passwordDecodificado; 
		p.setPassword(password);
		p.setActivo(rs.getBoolean("activo"));
		return p;	
	}
	
	public Usuario mapRowLogin(ResultSet rs) throws SQLException{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("user_name"));
		p.setPassword(rs.getString("password"));
		p.setActivo(rs.getBoolean("activo"));
		return p;
		
	}
}
