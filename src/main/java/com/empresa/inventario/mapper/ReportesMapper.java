package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;

public class ReportesMapper {
	
	public static ReportesMovimiento row(ResultSet resultSet) throws SQLException {
		ReportesMovimiento movimiento = new ReportesMovimiento();
		movimiento.setIdMovimiento(resultSet.getInt("idMovimiento"));
		movimiento.setFechaHora(resultSet.getDate("fechaHora"));
		movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
		movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
		movimiento.setCategoria(resultSet.getString("categoria"));
		movimiento.setTipoMovimiento(resultSet.getString("tipoMovimiento"));
		movimiento.setCantidad(Integer.toString(resultSet.getInt("cantidad")));
		movimiento.setUsuarioResponsable(resultSet.getString("responsable"));
		return movimiento;
	}
	
	public static ReporteInventarioValorizado rowInventariosValorizado(ResultSet resultSet) throws SQLException {
		ReporteInventarioValorizado movimiento = new ReporteInventarioValorizado();
		movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
		movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
		movimiento.setCategoria(resultSet.getString("categoria"));
		movimiento.setUbicacion(resultSet.getString("ubicacionProducto"));
		movimiento.setStockActual(resultSet.getString("stockActual"));
		movimiento.setUnidad(resultSet.getString("unidad"));
		movimiento.setPrecioUnitario(resultSet.getDouble("precioUnitario"));
		movimiento.setPrecioTotal(resultSet.getInt("valorTotal"));
		return movimiento;
	}
	
	public static ReporteStockBajo rowStockBajo(ResultSet resultSet) throws SQLException {
		ReporteStockBajo movimiento = new ReporteStockBajo();
		movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
		movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
		movimiento.setStockActual(resultSet.getString("stockActual"));
		movimiento.setStockMinimo(Integer.toString(resultSet.getInt("stockMinimo")));
		movimiento.setFaltanteSugerido(Integer.toString(resultSet.getInt("faltanteSugerido")));
		movimiento.setCategoria(resultSet.getString("categoria"));
		return movimiento;
	}
	
	public static ReporteAuditoriaUsuario rowAuditoriaUsuario(ResultSet resultSet) throws SQLException {
		ReporteAuditoriaUsuario movimiento = new ReporteAuditoriaUsuario();
		movimiento.setUsuarioResponsable(resultSet.getString("Operador"));
		movimiento.setFechaHora(resultSet.getDate("Fecha_Accion"));
		movimiento.setNombreProducto(resultSet.getString("Producto"));
		movimiento.setTipoMovimiento(resultSet.getString("Operacion"));
		movimiento.setCantidad(resultSet.getInt("cantidad"));
		movimiento.setObservaciones(resultSet.getString("justificacion"));

		return movimiento;
	}

	public static ReporteRotacionInventario rowRotacionInventario(ResultSet rs) throws SQLException {
		ReporteRotacionInventario movimiento = new ReporteRotacionInventario();
		movimiento.setNombreProducto(rs.getString("nombre"));
		movimiento.setTotalUnidadesSalida(rs.getInt("total_unidades_salida"));
		movimiento.setStockActual(rs.getInt("stock_disponible"));
		movimiento.setIndiceRotacion(rs.getDouble("indice_rotacion"));	
		return movimiento;
	}

	public static ReporteClasificacionABC rowClasificacionABC(ResultSet rs) throws SQLException {
		ReporteClasificacionABC abc = new ReporteClasificacionABC();
		abc.setNombreProducto(rs.getString("nombre"));
		abc.setValorStock(rs.getDouble("valorStock"));
		abc.setClasificacionABC(rs.getString("clasificacion_abc"));
		return abc;
	}
}
