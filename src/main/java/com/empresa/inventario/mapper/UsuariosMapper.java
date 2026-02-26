package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.utils.PasswordUtil;

public class UsuariosMapper {

	public Usuario mapRow(ResultSet rs) {
		Usuario p = new Usuario();
		try {
			p.setIdUsuario(rs.getInt("id_usuario"));
			p.setNombre(rs.getString("nombre"));
			p.setRol(rs.getString("rol"));
			p.setPermisos(rs.getString("permisos"));
			p.setUserName(rs.getString("user_name"));
			p.setPassword(PasswordUtil.decrypt(rs.getString("password")));
			p.setActivo(rs.getBoolean("activo"));
		} catch (Exception e) {
			e.getMessage();
		}
		return p;
	}

	public String mapRowUserName(ResultSet rs) {
		String userName = null;
		try {
			userName = rs.getNString("user_name");
		} catch (SQLException e) {
			e.getMessage();
		}
		return userName;
	}
}
