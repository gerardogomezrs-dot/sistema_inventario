package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Auditoria;

public class AuditoriaMapper {
	
	public AuditoriaMapper() {}

	public Auditoria mapRow(ResultSet rs) throws SQLException {
		Auditoria auditoria = new Auditoria();
		auditoria.setIdAuditoria(rs.getInt("id"));
		auditoria.setFechaAuditoria(rs.getDate("fecha_hora"));
		auditoria.setIdUsuario(rs.getInt("id_usuario"));
		auditoria.setClaseOrigen(rs.getString("clase_origen"));
		auditoria.setMetodo(rs.getString("metodo"));
		auditoria.setAccion(rs.getString("accion"));
		auditoria.setNivel(rs.getString("nivel"));
		return auditoria;
	}

}
