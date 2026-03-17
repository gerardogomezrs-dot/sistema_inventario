package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.MermasDevolucionesMapper;
import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.utils.Conexion;

public class MermasDevolucionesDAO {

	MermasDevolucionesMapper mapper;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MermasDevolucionesDAO.class);

	
	public void guardar(MermasDevoluciones mermasDevoluciones) {
		String sql = "INSERT INTO mermas_perdidas\r\n" + "(id_producto, cantidad, motivo, fecha, costo, tipo, id_usuario)\r\n"
				+ "VALUES( ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?)";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {
			ps.setInt(1, mermasDevoluciones.getIdProducto());
			ps.setInt(2, mermasDevoluciones.getCantidad());
			ps.setString(3, mermasDevoluciones.getMotivo());
			ps.setDouble(4, mermasDevoluciones.getCostoPerdido());
			ps.setString(5, mermasDevoluciones.getTipo());
			ps.setInt(6, mermasDevoluciones.getIdUsuario());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	public List<MermasDevoluciones> getAllMermasDevoluciones() {
		String sql = "SELECT \r\n"
				+ "    i.*, \r\n"
				+ "    p.id_producto AS idProducto, \r\n"
				+ "    p.nombre AS nombreProducto,\r\n"
				+ "    u.id_usuario AS idUsuario, \r\n"
				+ "    u.nombre AS nombreUsuario\r\n"
				+ "FROM inventarios.mermas_perdidas i \r\n"
				+ "INNER JOIN inventarios.productos p ON i.id_producto = p.id_producto\r\n"
				+ "INNER JOIN inventarios.usuarios u ON i.id_usuario = u.id_usuario;";
		List<MermasDevoluciones> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				MermasDevoluciones p = new MermasDevoluciones();
				mapper = new MermasDevolucionesMapper();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

}
