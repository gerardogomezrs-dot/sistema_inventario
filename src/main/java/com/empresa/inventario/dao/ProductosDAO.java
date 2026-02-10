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
		String sql = "SELECT p.*, c.id_categoria as idCategoria, c.nombre as "
				+ "nombreCategoria FROM productos p inner join categorias c on p.id_categoria = c.id_categoria";
		List<Productos> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Productos p = new Productos();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public void guardar(Productos productos) throws Exception {
		try {
			Connection conexion = Conexion.getConexion();

			String sql = "INSERT INTO productos "
					+ "(codigo_barras, nombre, descripcion, id_categoria, unidad, precio_unitario, "
					+ "stock_actual, stock_minimo, ubicacion, activo) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = conexion.prepareStatement(sql);

			ps.setString(1, productos.getCodigoBarras());
			ps.setString(2, productos.getNombre());
			ps.setString(3, productos.getDescripcion());
			ps.setInt(4, productos.getIdCategoria());
			ps.setString(5, productos.getUnidad());
			ps.setDouble(6, productos.getPrecioUnitario());
			ps.setInt(7, productos.getStockActual());
			ps.setInt(8, productos.getStockMinimo());
			ps.setString(9, productos.getUbicacion());
			ps.setBoolean(10, productos.isActivo());
			ps.executeUpdate();

			ps.close();
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void actualizar(Productos productos) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "UPDATE productos SET " + "codigo_barras = ?, " + "nombre = ?, " + "descripcion = ?, "
<<<<<<< HEAD
				+ "id_categoria = ?, " + "unidad = ?, " + "precio_unitario = ?, " + "stock_actual = ?, "
=======
				+ "categoria = ?, " + "unidad = ?, " + "precio_unitario = ?, " + "stock_actual = ?, "
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
				+ "stock_minimo = ?, " + "ubicacion = ?, " + "activo = ? " + "WHERE id_producto = ?";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, productos.getCodigoBarras());
		ps.setString(2, productos.getNombre());
		ps.setString(3, productos.getDescripcion());
		ps.setInt(4, productos.getIdCategoria());
		ps.setString(5, productos.getUnidad());
		ps.setDouble(6, productos.getPrecioUnitario());
		ps.setInt(7, productos.getStockActual());
		ps.setInt(8, productos.getStockMinimo());
		ps.setString(9, productos.getUbicacion());
		ps.setBoolean(10, productos.isActivo());
		ps.setInt(11, productos.getIdProducto());

		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

	public void actualizarStock(int idProducto, int stock) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "UPDATE productos SET stock_actual = ? " + "WHERE id_producto = ?";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setInt(1, stock);
		ps.setInt(2, idProducto);
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

	public int getByIdProducto(int idProducto) throws Exception {
		String sql = "SELECT stock_actual FROM productos where id_producto = ?";
		int p = 0;
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				p = rs.getInt("stock_actual");
<<<<<<< HEAD
=======
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return p;
	}

	public Productos getByIdCodigoBarras(String codigoBarras) throws Exception {
		String sql = "SELECT * FROM productos where codigo_barras = ?";
		Productos p = new Productos();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, codigoBarras);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.mapRowBy(rs);

>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return p;
	}

	public Productos getByIdCodigoBarras(String codigoBarras) throws Exception {
		String sql = "SELECT * FROM productos where codigo_barras = ?";
		Productos p = new Productos();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, codigoBarras);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.mapRowBy(rs);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return p;
	}
	
	
	public void bajaProducto(int idProducto) throws Exception {

		Connection connection = Conexion.getConexion();

		String sql = "UPDATE productos  SET activo = 0  WHERE ID_PRODUCTO = ?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, idProducto);
		statement.executeUpdate();
		statement.close();
		connection.close();

	}

}
