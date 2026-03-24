package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.ReportesAlmacenMapper;
import com.empresa.inventario.model.ReporteBajaRotacion;
import com.empresa.inventario.model.ReporteKardex;
import com.empresa.inventario.model.ReporteStockBajoAlmacen;
import com.empresa.inventario.model.ReportesExistencias;
import com.empresa.inventario.utils.Conexion;

public class ReportesAlmacenDAO {

	private ReportesAlmacenMapper mapper;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReportesAlmacenDAO.class);


	public List<ReportesExistencias> getInventarioValorizado() {
		String sql = "SELECT \r\n"
				+ "    p.codigo_barras AS 'Codigo',\r\n"
				+ "    p.nombre AS 'Producto',\r\n"
				+ "    c.nombre AS 'Categoria',\r\n"
				+ "    p.stock_actual AS 'Cantidad Disponible',\r\n"
				+ "    CONCAT(u.pasillo,' ', u.estante) as Ubicacion,\r\n"
				+ "    p.precio_unitario AS 'Precio Unitario'\r\n"
				+ "FROM productos p\r\n"
				+ "inner JOIN categorias c ON p.id_categoria = c.id_categoria\r\n"
				+ "INNER join ubicacion u on p.id_ubicacion  = u.id_ubicacion \r\n"
				+ "WHERE p.activo = 1\r\n"
				+ "ORDER BY c.nombre, p.nombre;";
		List<ReportesExistencias> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReportesExistencias p = new ReportesExistencias();
				mapper = new ReportesAlmacenMapper();
				p = mapper.rowReportesExistencias(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteBajaRotacion> getBajaRotacion() {
		String sql = "SELECT\r\n" + "    p.codigo_barras AS codigo,\r\n" + "    p.nombre,\r\n"
				+ "    p.stock_actual AS stock,\r\n" + "    MAX(m.fecha_hora) AS ultima_salida\r\n"
				+ "FROM productos p\r\n" + "LEFT JOIN movimientos m\r\n" + "    ON p.id_producto = m.id_producto\r\n"
				+ "    AND m.tipo_movimiento  = 'salida'\r\n" + "WHERE p.activo = 1\r\n" + "GROUP BY\r\n"
				+ "    p.id_producto,\r\n" + "    p.codigo_barras,\r\n" + "    p.nombre,\r\n" + "    p.stock_actual\r\n"
				+ "HAVING\r\n" + "    MAX(m.fecha_hora) < DATE_SUB(NOW(), INTERVAL 30 DAY)\r\n"
				+ "    OR MAX(m.fecha_hora) IS NULL\r\n" + "ORDER BY\r\n" + "    ultima_salida desc;";
		List<ReporteBajaRotacion> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteBajaRotacion p = new ReporteBajaRotacion();
				mapper = new ReportesAlmacenMapper();
				p = mapper.reporteBajaRotacion(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<ReporteStockBajoAlmacen> getStockCritico() {
		String sql = "SELECT \r\n" + "    codigo_barras , \r\n" + "    nombre, \r\n"
				+ "    stock_actual  AS 'Stock Actual', \r\n" + "    stock_minimo AS 'Mínimo Permitido',\r\n"
				+ "    (stock_minimo - stock_actual ) AS 'Faltante para Reorden'\r\n" + "FROM productos\r\n"
				+ "WHERE stock_actual  <= stock_minimo \r\n" + "AND activo = 1\r\n"
				+ "ORDER BY (stock_minimo - stock_actual) DESC;";
		List<ReporteStockBajoAlmacen> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteStockBajoAlmacen p = new ReporteStockBajoAlmacen();
				mapper = new ReportesAlmacenMapper();
				p = mapper.reporteStockBajo(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}
	
	public List<ReporteKardex> getReporteKardex() {
		String sql = "SELECT \r\n"
				+ "    m.fecha_hora AS 'Fecha/Hora',\r\n"
				+ "    m.tipo_movimiento AS 'Tipo',\r\n"
				+ "    -- Usamos COALESCE para evitar nulos en observaciones\r\n"
				+ "    COALESCE(m.observaciones, 'Sin observaciones') AS 'Concepto/Notas',\r\n"
				+ "    m.origen_destino AS 'Origen/Destino',\r\n"
				+ "    -- Diferenciamos visualmente Entradas de Salidas\r\n"
				+ "    CASE \r\n"
				+ "        WHEN m.tipo_movimiento IN ('Entrada', 'Compra', 'Ajuste Positivo') THEN m.cantidad \r\n"
				+ "        ELSE 0 \r\n"
				+ "    END AS 'Entrada',\r\n"
				+ "    CASE \r\n"
				+ "        WHEN m.tipo_movimiento IN ('Salida', 'Venta', 'Ajuste Negativo') THEN m.cantidad \r\n"
				+ "        ELSE 0 \r\n"
				+ "    END AS 'Salida',\r\n"
				+ "    -- El stock resultante es vital para la trazabilidad\r\n"
				+ "    m.cantidad AS 'Cantidad Movida', \r\n"
				+ "    u.nombre  AS 'Operador'\r\n"
				+ "FROM movimientos m\r\n"
				+ "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario;";
		List<ReporteKardex> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ReporteKardex p = new ReporteKardex();
				mapper = new ReportesAlmacenMapper();
				p = mapper.reporteKardex(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}
}
