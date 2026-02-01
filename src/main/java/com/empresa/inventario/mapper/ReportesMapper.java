package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public static ReportesMovimiento rowInventariosValorizado(ResultSet resultSet) throws SQLException {
		ReportesMovimiento movimiento = new ReportesMovimiento();
		movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
		movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
		movimiento.setCategoria(resultSet.getString("categoria"));
		movimiento.setUbicacion(resultSet.getString("ubicacionProducto"));
		movimiento.setStockActual(resultSet.getString("stockActual"));
		movimiento.setUnidad(resultSet.getString("unidad"));
		movimiento.setPrecioUnitario(resultSet.getInt("precioUnitario"));
		movimiento.setPrecioTotal(resultSet.getInt("valorTotal"));
		return movimiento;
	}
	
	public static ReportesMovimiento rowStockBajo(ResultSet resultSet) throws SQLException {
		ReportesMovimiento movimiento = new ReportesMovimiento();
		movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
		movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
		movimiento.setStockActual(resultSet.getString("stockActual"));
		movimiento.setStockMinimo(Integer.toString(resultSet.getInt("stockMinimo")));
		movimiento.setFaltanteSugerido(Integer.toString(resultSet.getInt("faltanteSugerido")));
		movimiento.setCategoria(resultSet.getString("categoria"));
		return movimiento;
	}

}
