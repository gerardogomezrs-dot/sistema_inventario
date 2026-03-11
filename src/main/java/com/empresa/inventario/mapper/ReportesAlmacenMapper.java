package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.ReporteBajaRotacion;
import com.empresa.inventario.model.ReporteKardex;
import com.empresa.inventario.model.ReporteStockBajoAlmacen;
import com.empresa.inventario.model.ReportesExistencias;

public class ReportesAlmacenMapper {

	public ReportesExistencias rowReportesExistencias(ResultSet resultSet) {
		ReportesExistencias existencias = new ReportesExistencias();
		try {
			existencias.setCodigo(resultSet.getString("Codigo"));
			existencias.setNombreProducto(resultSet.getString("Producto"));
			existencias.setNombreCategoria(resultSet.getString("Categoria"));
			existencias.setPrecioUnitario(resultSet.getDouble("Precio Unitario"));
			existencias.setStockDisponible(resultSet.getInt("Cantidad Disponible"));
			existencias.setUbicacion(resultSet.getString("Ubicacion"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return existencias;
	}

	public ReporteBajaRotacion reporteBajaRotacion(ResultSet rs) {
		ReporteBajaRotacion bajaRotacion = new ReporteBajaRotacion();

		try {
			bajaRotacion.setCodigoBarras(rs.getString("codigo"));
			bajaRotacion.setNombre(rs.getString("nombre"));
			bajaRotacion.setStockActual(rs.getInt("stock"));
			java.sql.Timestamp ts = rs.getTimestamp("ultima_salida");
			if (ts != null) {
				bajaRotacion.setUltimaSalida(new java.util.Date(ts.getTime()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bajaRotacion;
	}

	public ReporteStockBajoAlmacen reporteStockBajo(ResultSet rs) {
		ReporteStockBajoAlmacen stockBajo = new ReporteStockBajoAlmacen();
		try {
			stockBajo.setCodigoBarras(rs.getString("codigo_barras"));
			stockBajo.setNombreProducto(rs.getString("nombre"));
			stockBajo.setStockActual(rs.getInt("Stock Actual"));
			stockBajo.setStockPermitido(rs.getInt("Mínimo Permitido"));
			stockBajo.setFaltanteReorden(rs.getInt("Faltante para Reorden"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stockBajo;
	}

	public ReporteKardex reporteKardex(ResultSet rs) {
		ReporteKardex reporteKardex = new ReporteKardex();
		try {
			reporteKardex.setFecha(rs.getDate("Fecha/Hora"));
			reporteKardex.setTipoMovimiento(rs.getString("Tipo"));
			reporteKardex.setConcepto(rs.getString("Concepto/Notas"));
			reporteKardex.setOrigen("Origen/Destino");
			reporteKardex.setEntrada(rs.getString("Entrada"));
			reporteKardex.setSalida(rs.getString("Salida"));
			reporteKardex.setCantidadMovida(rs.getString("Cantidad Movida"));
			reporteKardex.setOperador(rs.getString("Operador"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reporteKardex;
	}

}
