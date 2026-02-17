package com.empresa.inventario.mapper;

import java.sql.ResultSet;

import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.utils.PasswordUtil;

public class UsuariosMapper {


	public Usuario mapRow(ResultSet rs) throws Exception{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("user_name"));
		String passwordDecodificado = PasswordUtil.decrypt(rs.getString("password"));
		String password = passwordDecodificado; 
		p.setPassword(password);
		p.setActivo(rs.getBoolean("activo"));
		return p;	
	}
	
	public Usuario mapRowLogin(ResultSet rs) throws Exception{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("user_name"));
		p.setPassword(PasswordUtil.decrypt(rs.getString("password")));
		p.setActivo(rs.getBoolean("activo"));
		return p;
		
	}
	
	public Usuario mapRowPerfil(ResultSet rs) throws Exception{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("user_name"));
		p.setPassword(PasswordUtil.decrypt(rs.getString("password")));
		p.setActivo(rs.getBoolean("activo"));
		return p;
		
	}

	public String mapRowUserName(ResultSet rs) throws Exception {
		String userName = rs.getNString("user_name");
		return userName;
	}
}
