package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.model.Ubicacion;

public class ProductosMapper {

	public Productos mapRow(ResultSet rs) throws SQLException {
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
		productos.setIdUbicacion(rs.getInt("id_ubicacion"));
		productos.setActivo(rs.getBoolean("activo"));
		productos.setIdProveedor(rs.getInt("id_proveedor"));
		Categorias categorias = new Categorias();
		categorias.setIdCategoria(rs.getInt("idCategoria"));
		categorias.setNombre(rs.getString("nombreCategoria"));
		productos.setCategorias(categorias);
		Proveedor proveedor = new Proveedor();
		proveedor.setIdProveedor(rs.getInt("idProveedor"));
		proveedor.setNombreEmpresa(rs.getString("nombreProveedor"));
		productos.setProveedor(proveedor);
		Ubicacion ubicacion = new Ubicacion();
		ubicacion.setPasillo(rs.getString("pasillo"));
		ubicacion.setEstante(rs.getString("estante"));
		productos.setUbicacion(ubicacion);
		productos.setArchivo(rs.getBytes("imagenProducto"));
		return productos;
	}

	public Productos mapRowBy(ResultSet rs) throws SQLException {
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
		productos.setIdUbicacion(rs.getInt("id_ubicacion"));
		productos.setActivo(rs.getBoolean("activo"));
		productos.setIdProveedor(rs.getInt("id_proveedor"));
		Categorias categorias = new Categorias();
		categorias.setIdCategoria(rs.getInt("idCategoria"));
		categorias.setNombre(rs.getString("nombreCategoria"));
		productos.setCategorias(categorias);
		Proveedor proveedor = new Proveedor();
		proveedor.setIdProveedor(rs.getInt("idProveedor"));
		proveedor.setNombreEmpresa(rs.getString("nombreProveedor"));
		productos.setProveedor(proveedor);
		Ubicacion ubicacion = new Ubicacion();
		ubicacion.setPasillo(rs.getString("pasillo"));
		ubicacion.setEstante(rs.getString("estante"));
		productos.setUbicacion(ubicacion);
		productos.setArchivo(rs.getBytes("imagenProducto"));
		return productos;
	}

	
	public Productos mapRowByIdInfoProducto(ResultSet rs) throws SQLException {
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
		productos.setIdUbicacion(rs.getInt("id_ubicacion"));
		productos.setActivo(rs.getBoolean("activo"));
		productos.setIdProveedor(rs.getInt("id_proveedor"));
		
		return productos;
	
	}

	
	public Productos mapRowStockBajo(ResultSet rs) throws SQLException {
		Productos productos = new Productos();
		productos.setIdProducto(rs.getInt("id_producto"));
		productos.setNombre(rs.getString("nombre"));
		productos.setStockActual(rs.getInt("stock_actual"));
		productos.setStockMinimo(rs.getInt("stock_minimo"));
		return productos;
	}

	public Productos mapRowSinExistencias(ResultSet rs) throws SQLException {
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
		p.setIdUbicacion(rs.getInt("id_ubicacion"));
		p.setActivo(rs.getBoolean("activo"));
		return p;
	}
}
