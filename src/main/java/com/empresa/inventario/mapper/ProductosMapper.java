package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Productos;

public class ProductosMapper {
	
	public ProductosMapper() {
		
	}
	
	public Productos mapRow(ResultSet rs) throws SQLException{
		Productos p = new Productos();
		p.setIdProducto(rs.getInt("id_producto"));
		p.setCodigoBarras(rs.getString("codigo_barras"));
		p.setNombre(rs.getString("nombre"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setCategoria(rs.getString("categoria"));
		p.setUnidad(rs.getString("unidad"));
		p.setStockActual(rs.getInt("stock_actual"));
		p.setStockMinimo(rs.getInt("stock_minimo"));
		p.setUbicacion(rs.getString("ubicacion"));
		p.setActivo(rs.getBoolean("activo"));
		return p;
	}
	
	

}
