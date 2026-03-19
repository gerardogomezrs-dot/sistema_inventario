package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.ReportesMapper;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteMermasDevolucion;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.utils.Conexion;

public class ReportesDAO {
	
	
	private  ReportesMapper mapper = new ReportesMapper();
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReportesDAO.class);


	public List<ReportesMovimiento> getAllCategorias(LocalDateTime dateTimeInicio, LocalDateTime dateTimeFin) {

		StringBuilder sql = new StringBuilder(
				"SELECT " + "    m.id_movimiento as idMovimiento, " + "    m.fecha_hora as fechaHora, "
						+ "    p.codigo_barras as codigoB, " + "    p.nombre AS nombreProductos, "
						+ "    c.nombre AS nombreCategoria, " + "    m.tipo_movimiento as tipoMovimiento, "
						+ "    m.cantidad as cantidadProducto, " + "    m.origen_destino as origenDestino, "
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
					lista.add(mapper.row(rs));
				}
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteInventarioValorizado> getInventarioValorizado() {
		String sql = "SELECT \r\n" + "    p.codigo_barras as codigoBarra,\r\n" + "    p.nombre as producto,\r\n"
				+ "    c.nombre AS categoria,\r\n" + "    p.ubicacion as ubicacionProducto,\r\n"
				+ "    p.stock_actual as stockActual,\r\n" + "    p.unidad as unidad,\r\n"
				+ "    p.precio_unitario as precioUnitario,\r\n"
				+ "    (p.stock_actual * p.precio_unitario) AS valorTotal\r\n" + "FROM productos p\r\n"
				+ "JOIN categorias c ON p.id_categoria = c.id_categoria\r\n" + "WHERE p.activo = 1\r\n"
				+ "ORDER BY valorTotal DESC;";
		List<ReporteInventarioValorizado> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteInventarioValorizado p = new ReporteInventarioValorizado();
				p = mapper.rowInventariosValorizado(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteStockBajo> getStockBajo() {
		String sql = "SELECT \r\n" + "    p.codigo_barras as codigoBarras,\r\n" + "    p.nombre as nombreProducto,\r\n"
				+ "    p.stock_actual as stockActual,\r\n" + "    p.stock_minimo as stockMinimo,\r\n"
				+ "    (p.stock_minimo - p.stock_actual) AS faltanteSugerido,\r\n" + "    c.nombre AS categoria\r\n"
				+ "FROM productos p\r\n" + "INNER JOIN categorias c ON p.id_categoria = c.id_categoria\r\n"
				+ "WHERE p.stock_actual <= p.stock_minimo \r\n" + "AND p.activo = 1";
		List<ReporteStockBajo> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteStockBajo p = new ReporteStockBajo();
				p = mapper.rowStockBajo(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteAuditoriaUsuario> getAuditoriaUsuario() {
		String sql = "SELECT \r\n" + "    u.user_name AS Operador,\r\n" + "    m.fecha_hora AS Fecha_Accion,\r\n"
				+ "    p.nombre AS Producto,\r\n" + "    m.tipo_movimiento AS Operacion,\r\n"
				+ "    m.cantidad AS Cantidad,\r\n" + "    m.observaciones AS Justificacion \r\n"
				+ "FROM movimientos m \r\n" + "JOIN usuarios u ON m.id_usuario = u.id_usuario \r\n"
				+ "JOIN productos p ON m.id_producto = p.id_producto  \r\n"
				+ "ORDER BY m.fecha_hora DESC";
		List<ReporteAuditoriaUsuario> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteAuditoriaUsuario p = new ReporteAuditoriaUsuario();
				p = mapper.rowAuditoriaUsuario(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteRotacionInventario> getRotacionInventario() {
		String sql = "SELECT " + "    p.nombre, " + "    SUM(m.cantidad) AS total_unidades_salida, "
				+ "    p.stock_actual AS stock_disponible, "
				+ "    ROUND(SUM(m.cantidad) / NULLIF(p.stock_actual, 0), 2) AS indice_rotacion " + "FROM productos p "
				+ "JOIN movimientos m ON p.id_producto = m.id_producto " + "WHERE m.tipo_movimiento = 'SALIDA' "
				+ "  AND m.fecha_hora >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " + "GROUP BY p.id_producto "
				+ "ORDER BY indice_rotacion DESC;";
		List<ReporteRotacionInventario> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteRotacionInventario p = new ReporteRotacionInventario();
				p = mapper.rowRotacionInventario(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteClasificacionABC> getClasificacionABC() {
		String sql = "WITH Valorizacion AS (\r\n" + "    SELECT \r\n" + "        id_producto, \r\n"
				+ "        nombre AS nombreProducto, \r\n"
				+ "        (stock_actual * precio_unitario) AS valorStock \r\n" + "    FROM productos\r\n" + "), \r\n"
				+ "Acumulados AS (\r\n" + "    SELECT \r\n" + "        nombreProducto, \r\n"
				+ "        valorStock, \r\n" + "        -- Cálculo del porcentaje acumulado sobre el total\r\n"
				+ "        SUM(valorStock) OVER (ORDER BY valorStock DESC) / SUM(valorStock) OVER () * 100 AS porcentaje_acumulado\r\n"
				+ "    FROM Valorizacion\r\n" + ") \r\n" + "SELECT \r\n" + "    nombreProducto, \r\n"
				+ "    valorStock, \r\n" + "    CASE \r\n"
				+ "        WHEN porcentaje_acumulado <= 80 THEN 'A (Alta Prioridad)' \r\n"
				+ "        WHEN porcentaje_acumulado <= 95 THEN 'B (Media Prioridad)' \r\n"
				+ "        ELSE 'C (Baja Prioridad)' \r\n" + "    END AS clasificacion_abc\r\n" + "FROM Acumulados\r\n"
				+ "ORDER BY valorStock DESC";
		List<ReporteClasificacionABC> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteClasificacionABC p = new ReporteClasificacionABC();
				p = mapper.rowClasificacionABC(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}
	
	public List<ReporteMermasDevolucion> getReporteMermasDevoluciones() {
		String sql = "SELECT \r\n"
				+ "    m.fecha, \r\n"
				+ "    p.nombre, \r\n"
				+ "    m.tipo, -- Merma o Devolución\r\n"
				+ "    u.nombre AS responsable, \r\n"
				+ "    u.rol, \r\n"
				+ "    m.cantidad,\r\n"
				+ "    (m.cantidad * p.precio_unitario) AS total_perdida\r\n"
				+ "FROM mermas_perdidas m\r\n"
				+ "JOIN usuarios u ON m.id_usuario = u.id_usuario\r\n"
				+ "JOIN productos p ON m.id_producto = p.id_producto\r\n"
				+ "WHERE u.activo = 1; -- Filtro usando tu campo 'activo'";
		List<ReporteMermasDevolucion> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteMermasDevolucion p = new ReporteMermasDevolucion();
				p = mapper.rowMermasDevolucion(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}
}
