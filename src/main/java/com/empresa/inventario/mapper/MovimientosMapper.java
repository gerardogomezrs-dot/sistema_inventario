package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;

public class MovimientosMapper {

	public Movimientos mapRow(ResultSet rs) throws SQLException {
		Movimientos p = new Movimientos();
		p.setIdMovimiento(rs.getInt("id_movimiento"));
		p.setIdProducto(rs.getInt("id_producto"));
		p.setTipoMovimiento(rs.getString("tipo_movimiento"));
		p.setCantidad(rs.getInt("cantidad"));
		p.setFechaHora(rs.getDate("fecha_hora"));
		p.setOrigenDestino(rs.getString("origen_destino"));
		p.setIdUsuario(rs.getInt("id_usuario"));
		p.setObservaciones(rs.getString("observaciones"));
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(rs.getInt("idUsuario"));
		usuario.setNombre(rs.getString("nombreUsuario"));
		p.setUsuario(usuario);
		Productos productos = new Productos();
		productos.setIdProducto(rs.getInt("idProducto"));
		productos.setNombre(rs.getString("nombreProducto"));
		p.setProductos(productos);
		return p;

	}

}
