package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.AuditoriaMapper;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.utils.Conexion;

public class AuditoriaDao {

	private AuditoriaMapper auditoriaMapper = new AuditoriaMapper();

	public List<Auditoria> getAllAuditoria() {
		String sql = "SELECT a.* FROM auditoria_sistema a";
		List<Auditoria> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Auditoria p = new Auditoria();
				auditoriaMapper = new AuditoriaMapper();
				p = auditoriaMapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	public void guardar(Auditoria e) {
		String sql = "INSERT INTO inventarios.auditoria_sistema\r\n"
				+ "(fecha_hora, id_usuario, clase_origen, metodo, accion, nivel)\r\n"
				+ "VALUES(CURRENT_TIMESTAMP, ?, ?, ?, ?, ?);";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setInt(1, e.getIdUsuario());
			ps.setString(2, e.getClaseOrigen());
			ps.setString(3, e.getMetodo());
			ps.setString(4, e.getAccion());
			ps.setString(5, e.getNivel());

			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
