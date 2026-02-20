package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.ReportesMapper;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.utils.Conexion;

public class ReportesDAO {

	public List<ReportesMovimiento> getAllCategorias(LocalDateTime dateTimeInicio, LocalDateTime dateTimeFin)
			throws Exception {

		StringBuilder sql = new StringBuilder(
				"SELECT " + "    m.id_movimiento as idMovimiento, " + "    m.fecha_hora as fechaHora, "
						+ "    p.codigo_barras as codigoBarras, " + "    p.nombre AS nombreProducto, "
						+ "    c.nombre AS categoria, " + "    m.tipo_movimiento as tipoMovimiento, "
						+ "    m.cantidad as cantidad, " + "    m.origen_destino as origenDestino, "
						+ "    u.nombre AS responsable, " + "    m.observaciones as observaciones "
						+ "FROM movimientos m " + "INNER JOIN productos p ON m.id_producto = p.id_producto "
						+ "INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
						+ "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario ");

		boolean filtrarFechas = (dateTimeInicio != null && dateTimeFin != null);

		if (filtrarFechas) {
			sql.append(" WHERE m.fecha_hora BETWEEN ? AND ? ");
		}

		sql.append(" ORDER BY m.fecha_hora DESC");

		List<ReportesMovimiento> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

			if (filtrarFechas) {
				ps.setObject(1, dateTimeInicio);
				ps.setObject(2, dateTimeFin);
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(ReportesMapper.row(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public List<ReporteInventarioValorizado> getInventarioValorizado() throws Exception {
		String sql = "SELECT \r\n" + "    p.codigo_barras as codigoBarras,\r\n" + "    p.nombre as nombreProducto,\r\n"
				+ "    c.nombre AS categoria,\r\n" + "    p.ubicacion as ubicacionProducto,\r\n"
				+ "    p.stock_actual as stockActual,\r\n" + "    p.unidad as unidad,\r\n"
				+ "    p.precio_unitario as precioUnitario,\r\n"
				+ "    (p.stock_actual * p.precio_unitario) AS valorTotal\r\n" + "FROM productos p\r\n"
				+ "JOIN categorias c ON p.id_categoria = c.id_categoria\r\n" + "WHERE p.activo = 1\r\n"
				+ "ORDER BY valorTotal DESC;";
		List<ReporteInventarioValorizado> lista = new ArrayList<ReporteInventarioValorizado>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteInventarioValorizado p = new ReporteInventarioValorizado();
				p = ReportesMapper.rowInventariosValorizado(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public List<ReporteStockBajo> getStockBajo() throws Exception {
		String sql = "SELECT \r\n" + "    p.codigo_barras as codigoBarras,\r\n" + "    p.nombre as nombreProducto,\r\n"
				+ "    p.stock_actual as stockActual,\r\n" + "    p.stock_minimo as stockMinimo,\r\n"
				+ "    (p.stock_minimo - p.stock_actual) AS faltanteSugerido,\r\n" + "    c.nombre AS categoria\r\n"
				+ "FROM productos p\r\n" + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria\r\n"
				+ "WHERE p.stock_actual <= p.stock_minimo \r\n" + "AND p.activo = 1";
		List<ReporteStockBajo> lista = new ArrayList<ReporteStockBajo>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteStockBajo p = new ReporteStockBajo();
				p = ReportesMapper.rowStockBajo(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public List<ReporteAuditoriaUsuario> getAuditoriaUsuario() throws Exception {
		String sql = "SELECT \r\n" + "    u.user_name AS Operador,\r\n" + "    m.fecha_hora AS Fecha_Accion,\r\n"
				+ "    p.nombre AS Producto,\r\n" + "    m.tipo_movimiento AS Operacion,\r\n"
				+ "    m.cantidad AS Cantidad,\r\n" + "    m.observaciones AS Justificacion \r\n"
				+ "FROM movimientos m \r\n" + "JOIN usuarios u ON m.id_usuario = u.id_usuario \r\n"
				+ "JOIN productos p ON m.id_producto = p.id_producto \r\n" + "WHERE m.tipo_movimiento = 'AJUSTE' \r\n"
				+ "ORDER BY m.fecha_hora DESC";
		List<ReporteAuditoriaUsuario> lista = new ArrayList<ReporteAuditoriaUsuario>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteAuditoriaUsuario p = new ReporteAuditoriaUsuario();
				p = ReportesMapper.rowAuditoriaUsuario(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}
	
	public List<ReporteRotacionInventario> getRotacionInventario() throws Exception {
		String sql = "SELECT "
				+ "    p.nombre, "
				+ "    SUM(m.cantidad) AS total_unidades_salida, "
				+ "    p.stock_actual AS stock_disponible, "
				+ "    ROUND(SUM(m.cantidad) / NULLIF(p.stock_actual, 0), 2) AS indice_rotacion "
				+ "FROM productos p "
				+ "JOIN movimientos m ON p.id_producto = m.id_producto "
				+ "WHERE m.tipo_movimiento = 'SALIDA' "
				+ "  AND m.fecha_hora >= DATE_SUB(NOW(), INTERVAL 3 MONTH) "
				+ "GROUP BY p.id_producto "
				+ "ORDER BY indice_rotacion DESC;";
		List<ReporteRotacionInventario> lista = new ArrayList<ReporteRotacionInventario>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteRotacionInventario p = new ReporteRotacionInventario();
				p = ReportesMapper.rowRotacionInventario(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}
	
	public List<ReporteClasificacionABC> getClasificacionABC() throws Exception {
		String sql = "WITH Valorizacion AS ( "
				+ "    SELECT "
				+ "        id_producto, "
				+ "        nombre, "
				+ "        (stock_actual * precio_unitario) AS valorStock "
				+ "    FROM productos "
				+ "), "
				+ "Acumulados AS ( "
				+ "    SELECT "
				+ "        nombre, "
				+ "        valorStock, "
				+ "        SUM(valorStock) OVER (ORDER BY valorStock DESC) / SUM(valorStock) OVER () * 100 AS porcentaje_acumulado "
				+ "    FROM Valorizacion "
				+ ") "
				+ "SELECT "
				+ "    nombre, "
				+ "    valorStock, "
				+ "    CASE "
				+ "        WHEN porcentaje_acumulado <= 80 THEN 'A (Alta Prioridad)' "
				+ "        WHEN porcentaje_acumulado <= 95 THEN 'B (Media Prioridad)' "
				+ "        ELSE 'C (Baja Prioridad)' "
				+ "    END AS clasificacion_abc "
				+ "FROM Acumulados ";
		List<ReporteClasificacionABC> lista = new ArrayList<ReporteClasificacionABC>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteClasificacionABC p = new ReporteClasificacionABC();
				p = ReportesMapper.rowClasificacionABC(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}
}

