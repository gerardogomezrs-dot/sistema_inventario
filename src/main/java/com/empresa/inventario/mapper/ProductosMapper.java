package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;

public class ProductosMapper {
	
	public Productos mapRow(ResultSet rs) throws SQLException{
		Productos productos = new Productos();
		productos.setIdProducto(rs.getInt("id_producto"));
		productos.setCodigoBarras(rs.getString("codigo_barras"));
		productos.setNombre(rs.getString("nombre"));
		productos.setDescripcion(rs.getString("descripcion"));
		productos.setIdCategoria(rs.getInt("id_categoria"));
		productos.setUnidad(rs.getString("unidad"));
		productos.setPrecioUnitario(rs.getDouble("precio_unitario"));
		productos.setStockActual(rs.getInt("stock_actual"));
		productos.setStockMinimo(rs.getInt("stock_minimo"));
		productos.setUbicacion(rs.getString("ubicacion"));
		productos.setActivo(rs.getBoolean("activo"));
		Categorias categorias = new Categorias();
		categorias.setIdCategoria(rs.getInt("idCategoria"));
		categorias.setNombre(rs.getString("nombreCategoria"));
		productos.setCategorias(categorias);
		return productos;
	}
	
	public Productos mapRowBy(ResultSet rs) throws SQLException{
		Productos p = new Productos();
		p.setIdProducto(rs.getInt("id_producto"));
		p.setCodigoBarras(rs.getString("codigo_barras"));
		p.setNombre(rs.getString("nombre"));
		p.setDescripcion(rs.getString("descripcion"));
		p.setIdCategoria(rs.getInt("id_categoria"));
		p.setUnidad(rs.getString("unidad"));
		p.setPrecioUnitario(rs.getDouble("precio_unitario"));
		p.setStockActual(rs.getInt("stock_actual"));
		p.setStockMinimo(rs.getInt("stock_minimo"));
		p.setUbicacion(rs.getString("ubicacion"));
		p.setActivo(rs.getBoolean("activo"));
		return p;
	}
}
