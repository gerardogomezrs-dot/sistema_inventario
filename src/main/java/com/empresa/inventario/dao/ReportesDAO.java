package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.ReportesMapper;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.utils.Conexion;

public class ReportesDAO {
	

	public List<ReportesMovimiento> getAllCategorias(LocalDateTime dateTimeInicio, LocalDateTime dateTimeFin) throws Exception{
		String sql = "SELECT \r\n"
				+ "    m.id_movimiento as idMovimiento,\r\n"
				+ "    m.fecha_hora as fechaHora,\r\n"
				+ "    p.codigo_barras as codigoBarras,\r\n"
				+ "    p.nombre AS nombreProducto ,\r\n"
				+ "    c.nombre AS categoria,\r\n"
				+ "    m.tipo_movimiento as tipoMovimiento, -- 'ENTRADA' o 'SALIDA'\r\n"
				+ "    m.cantidad as cantidad,\r\n"
				+ "    m.origen_destino as origenDestino,\r\n"
				+ "    u.nombre AS responsable,\r\n"
				+ "    m.observaciones as observaciones\r\n"
				+ "FROM movimientos m\r\n"
				+ "INNER JOIN productos p ON m.id_producto = p.id_producto\r\n"
				+ "INNER JOIN categorias c ON p.id_categoria = c.id_categoria\r\n"
				+ "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario\r\n"
				+ "ORDER BY m.fecha_hora DESC ";
		List<ReportesMovimiento> lista = new ArrayList<ReportesMovimiento>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			// Usamos while para iterar sobre cada fila del ResultSet
			while (rs.next()) {
				ReportesMovimiento p = new ReportesMovimiento();
				p = ReportesMapper.row(rs);
				// Agregamos el producto a la lista en cada iteración
				lista.add(p);
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

}
