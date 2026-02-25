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

	public ReportesMapper() {
		// TODO Auto-generated constructor stub
	}

	public  ReportesMovimiento row(ResultSet resultSet) {
		ReportesMovimiento movimiento = new ReportesMovimiento();

		try {
			movimiento.setIdMovimiento(resultSet.getInt("idMovimiento"));
			movimiento.setFechaHora(resultSet.getDate("fechaHora"));
			movimiento.setCodigoBarras(resultSet.getString("codigoB"));
			movimiento.setNombreProducto(resultSet.getString("nombreProductos"));
			movimiento.setCategoria(resultSet.getString("nombreCategoria"));
			movimiento.setTipoMovimiento(resultSet.getString("tipoMovimiento"));
			movimiento.setCantidad(Integer.toString(resultSet.getInt("cantidad")));
			movimiento.setUsuarioResponsable(resultSet.getString("responsable"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movimiento;
	}

	public  ReporteInventarioValorizado rowInventariosValorizado(ResultSet resultSet) {
		ReporteInventarioValorizado movimiento = new ReporteInventarioValorizado();

		try {
			movimiento.setCodigoBarras(resultSet.getString("codigoBarra"));
			movimiento.setNombreProducto(resultSet.getString("producto"));
			movimiento.setCategoria(resultSet.getString("categoria"));
			movimiento.setUbicacion(resultSet.getString("ubicacionProducto"));
			movimiento.setStockActual(resultSet.getString("stockActual"));
			movimiento.setUnidad(resultSet.getString("unidad"));
			movimiento.setPrecioUnitario(resultSet.getDouble("precioUnitario"));
			movimiento.setPrecioTotal(resultSet.getInt("valorTotal"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movimiento;
	}

	public  ReporteStockBajo rowStockBajo(ResultSet resultSet) {
		ReporteStockBajo movimiento = new ReporteStockBajo();
		
		try {
			movimiento.setCodigoBarras(resultSet.getString("codigoBarras"));
			movimiento.setNombreProducto(resultSet.getString("nombreProducto"));
			movimiento.setStockActual(resultSet.getString("stockActual"));
			movimiento.setStockMinimo(Integer.toString(resultSet.getInt("stockMinimo")));
			movimiento.setFaltanteSugerido(Integer.toString(resultSet.getInt("faltanteSugerido")));
			movimiento.setCategoria(resultSet.getString("categoria"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movimiento;
	}

	public  ReporteAuditoriaUsuario rowAuditoriaUsuario(ResultSet resultSet) {
		ReporteAuditoriaUsuario movimiento = new ReporteAuditoriaUsuario();
		
		try {
			movimiento.setUsuarioResponsable(resultSet.getString("Operador"));
			movimiento.setFechaHora(resultSet.getDate("Fecha_Accion"));
			movimiento.setNombreProducto(resultSet.getString("Producto"));
			movimiento.setTipoMovimiento(resultSet.getString("Operacion"));
			movimiento.setCantidad(resultSet.getInt("cantidad"));
			movimiento.setObservaciones(resultSet.getString("justificacion"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movimiento;
	}

	public ReporteRotacionInventario rowRotacionInventario(ResultSet rs) {
		ReporteRotacionInventario movimiento = new ReporteRotacionInventario();
		
		try {
			movimiento.setNombreProducto(rs.getString("nombre"));
			movimiento.setTotalUnidadesSalida(rs.getInt("total_unidades_salida"));
			movimiento.setStockActual(rs.getInt("stock_disponible"));
			movimiento.setIndiceRotacion(rs.getDouble("indice_rotacion"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movimiento;
	}

	public  ReporteClasificacionABC rowClasificacionABC(ResultSet rs) {
		ReporteClasificacionABC abc = new ReporteClasificacionABC();
		
		try {
			abc.setNombreProducto(rs.getString("nombreProducto"));
			abc.setValorStock(rs.getDouble("valorStock"));
			abc.setClasificacionABC(rs.getString("clasificacion_abc"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return abc;
	}
}
