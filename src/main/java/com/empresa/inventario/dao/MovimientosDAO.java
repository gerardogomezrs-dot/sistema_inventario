package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.MovimientosMapper;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.utils.Conexion;

public class MovimientosDAO {
	
	private MovimientosMapper mapper = new MovimientosMapper();
	
	public List<Movimientos> getAll() throws Exception {
		
		String sql = "SELECT m.*, u.id_usuario AS idUsuario, u.nombre AS nombreUsuario, p.id_producto as idProducto, "
				+ "p.nombre as nombreProducto FROM MOVIMIENTOS m " +
                "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
                +"INNER JOIN productos p on  m.id_Producto = p.id_producto";
		
		List<Movimientos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			// Usamos while para iterar sobre cada fila del ResultSet
			while (rs.next()) {
				Movimientos p = new Movimientos();
				p = mapper.mapRow(rs);
				// Agregamos el producto a la lista en cada iteración
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	
	public void guardar(Movimientos movimientos) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "INSERT INTO movimientos (id_producto, "
				+ " tipo_movimiento , cantidad, fecha_hora ,  origen_destino ,  id_usuario ,  observaciones ) "
				+ " VALUES "+ 
				"(?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setInt(1, movimientos.getIdProducto());
		ps.setString(2, movimientos.getTipoMovimiento());
		ps.setInt(3, movimientos.getCantidad());
		ps.setTimestamp(4, new java.sql.Timestamp(movimientos.getFechaHora().getTime()));
		ps.setString(5, movimientos.getOrigenDestino());
		ps.setInt(6, movimientos.getIdUsuario());
		ps.setString(7, movimientos.getObservaciones());
		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

}
