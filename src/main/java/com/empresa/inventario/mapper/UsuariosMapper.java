package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Usuario;

public class UsuariosMapper {
	
	public UsuariosMapper() {
		
	}

	public Usuario mapRow(ResultSet rs) throws SQLException{
		Usuario p = new Usuario();
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setNombre(rs.getString("nombre"));
		p.setRol(rs.getString("rol"));
		p.setPermisos(rs.getString("permisos"));
		p.setUserName(rs.getString("userName"));
		p.setPassword(rs.getString("password"));
		p.setActivo(rs.getBoolean("activo"));
		return p;
		
	}
}
