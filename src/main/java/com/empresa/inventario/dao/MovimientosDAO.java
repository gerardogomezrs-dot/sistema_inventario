package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.MovimientosMapper;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.utils.Conexion;

public class MovimientosDAO {

	private MovimientosMapper mapper = new MovimientosMapper();

	public List<Movimientos> getAll() {

		String sql = "SELECT m.*, u.id_usuario AS idUsuario, u.nombre AS nombreUsuario, p.id_producto as idProducto, "
				+ "p.nombre as nombreProducto FROM MOVIMIENTOS m "
				+ "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
				+ "INNER JOIN productos p on  m.id_Producto = p.id_producto";

		List<Movimientos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Movimientos p = new Movimientos();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.getMessage();
		}
		return lista;
	}

	public void guardar(Movimientos movimientos) {
		String sql = "INSERT INTO movimientos (id_producto, "
				+ " tipo_movimiento , cantidad, fecha_hora ,  origen_destino ,  id_usuario ,  observaciones ) "
				+ " VALUES " + "(?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setInt(1, movimientos.getIdProducto());
			ps.setString(2, movimientos.getTipoMovimiento());
			ps.setInt(3, movimientos.getCantidad());
			ps.setTimestamp(4, new Timestamp(movimientos.getFechaHora().getTime()));
			ps.setString(5, movimientos.getOrigenDestino());
			ps.setInt(6, movimientos.getIdUsuario());
			ps.setString(7, movimientos.getObservaciones());
			ps.executeUpdate();
		} catch (Exception e) {
			e.getMessage();
		}

	}
	
	public List<Movimientos> getByUsuario(int idUsuario) {
	    // 1. Usamos un alias claro y la cláusula WHERE
	    String sql = "SELECT m.*, u.nombre AS nombreUsuario, p.nombre AS nombreProducto "
	               + "FROM MOVIMIENTOS m "
	               + "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
	               + "INNER JOIN productos p ON m.id_producto = p.id_producto "
	               + "WHERE m.id_usuario = ?"; // Filtro paramétrico

	    List<Movimientos> lista = new ArrayList<>();

	    // 2. Uso de Try-with-resources para auto-cerrar la conexión
	    try (Connection con = Conexion.getConexion();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        // 3. Seteamos el parámetro de forma segura
	        ps.setInt(1, idUsuario);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                // El mapper se encarga de la lógica de conversión
	                lista.add(mapper.mapRow(rs));
	            }
	        }
	    } catch (SQLException e) {
	    	e.getMessage();
	    }
	    return lista;
	}


}
