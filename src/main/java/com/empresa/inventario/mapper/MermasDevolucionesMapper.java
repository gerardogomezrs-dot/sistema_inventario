package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;

public class MermasDevolucionesMapper {
	
	public MermasDevoluciones mapRow(ResultSet resultSet) throws SQLException {
		MermasDevoluciones mermasDevoluciones = new MermasDevoluciones();
		mermasDevoluciones.setIdMerma(resultSet.getInt("id_merma"));
		mermasDevoluciones.setIdProducto(resultSet.getInt("id_producto"));
		mermasDevoluciones.setCantidad(resultSet.getInt("cantidad"));
		mermasDevoluciones.setMotivo(resultSet.getString("motivo"));
		mermasDevoluciones.setFecha(resultSet.getDate("fecha"));
		mermasDevoluciones.setCostoPerdido(resultSet.getDouble("costo"));
		mermasDevoluciones.setTipo(resultSet.getString("tipo"));
		Productos productos = new Productos();
		productos.setIdProducto(resultSet.getInt("idProducto"));
		productos.setNombre(resultSet.getString("nombreProducto"));
		mermasDevoluciones.setProducto(productos);
		Usuario usuario= new Usuario();
		usuario.setIdUsuario(resultSet.getInt("idUsuario"));
		usuario.setNombre(resultSet.getString("nombreUsuario"));
		mermasDevoluciones.setUsuario(usuario);
		return mermasDevoluciones;
	}
}
