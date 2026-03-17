package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.MovimientosMapper;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.utils.Conexion;

public class MovimientosDAO {

	private MovimientosMapper mapper = new MovimientosMapper();

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovimientosDAO.class);

	public List<Movimientos> getAll() {

		String sql = "SELECT m.*, u.id_usuario AS idUsuario, u.nombre AS nombreUsuario, p.id_producto as idProducto, "
				+ "p.nombre as nombreProducto FROM MOVIMIENTOS m "
				+ "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
				+ "INNER JOIN productos p on  m.id_Producto = p.id_producto";

		List<Movimientos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Movimientos p = new Movimientos();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public void guardar(Movimientos movimientos) {
		String sql = "INSERT INTO movimientos (id_producto, "
				+ " tipo_movimiento , cantidad, fecha_hora ,  origen_destino ,  id_usuario ,  observaciones, stock_previo, stock_posterior ) "
				+ " VALUES " + "(?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {
			ps.setInt(1, movimientos.getIdProducto());
			ps.setString(2, movimientos.getTipoMovimiento());
			ps.setInt(3, movimientos.getCantidad());
			ps.setString(4, movimientos.getOrigenDestino());
			ps.setInt(5, movimientos.getIdUsuario());
			ps.setString(6, movimientos.getObservaciones());
			ps.setInt(7, movimientos.getStockPrevio());
			ps.setInt(8, movimientos.getStockPosterior());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	}

	public List<Movimientos> getByUsuario(int idUsuario) {

		String sql = "SELECT m.*, u.nombre AS nombreUsuario, p.nombre AS nombreProducto " + "FROM MOVIMIENTOS m "
				+ "LEFT JOIN usuarios u ON m.id_usuario = u.id_usuario "
				+ "LEFT JOIN productos p ON m.id_producto = p.id_producto " + "WHERE m.id_usuario = ?"; // Filtro
																										// paramétrico
		List<Movimientos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idUsuario);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					lista.add(mapper.mapRowById(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return lista;
	}

}
