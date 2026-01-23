package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.ProductosMapper;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.utils.Conexion;

public class ProductosDAO {

	private ProductosMapper mapper = new ProductosMapper();

	public List<Productos> getAll() throws Exception {
		String sql = "SELECT * FROM productos";
		List<Productos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			// Usamos while para iterar sobre cada fila del ResultSet
			while (rs.next()) {
				Productos p = new Productos();
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

	public void guardar(Productos productos) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "INSERT INTO productos " + "(codigo_barras, nombre, descripcion, categoria, unidad, "
				+ "stock_actual, stock_minimo, ubicacion, activo) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, productos.getCodigoBarras());
		ps.setString(2, productos.getNombre());
		ps.setString(3, productos.getDescripcion());
		ps.setString(4, productos.getCategoria());
		ps.setString(5, productos.getUnidad());
		ps.setInt(6, productos.getStockActual());
		ps.setInt(7, productos.getStockMinimo());
		ps.setString(8, productos.getUbicacion());
		ps.setBoolean(9, productos.isActivo());
		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

	public void actualizar(Productos productos) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "UPDATE productos SET " + "codigo_barras = ?, " + "nombre = ?, " + "descripcion = ?, "
				+ "categoria = ?, " + "unidad = ?, " + "stock_actual = ?, " + "stock_minimo = ?, " + "ubicacion = ?, "
				+ "activo = ? " + "WHERE id_producto = ?";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, productos.getCodigoBarras());
		ps.setString(2, productos.getNombre());
		ps.setString(3, productos.getDescripcion());
		ps.setString(4, productos.getCategoria());
		ps.setString(5, productos.getUnidad());
		ps.setInt(6, productos.getStockActual());
		ps.setInt(7, productos.getStockMinimo());
		ps.setString(8, productos.getUbicacion());
		ps.setBoolean(9, productos.isActivo());
		ps.setInt(10, productos.getIdProducto());

		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

	public void eliminarProducto(int idProducto) throws Exception {

		Connection connection = Conexion.getConexion();

		String sql = "DELETE FROM PRODUCTOS WHERE ID_PRODUCTO = ?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, idProducto);
		statement.executeUpdate();
		statement.close();
		connection.close();

	}

}
