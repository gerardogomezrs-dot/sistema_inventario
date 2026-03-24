package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.ProductosMapper;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.utils.Conexion;

public class ProductosDAO {

	private ProductosMapper mapper = new ProductosMapper();

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductosDAO.class);

	public List<Productos> getAll() {
		String sql = "SELECT p.*, c.id_categoria AS idCategoria, \r\n"
				+ "				     c.nombre AS nombreCategoria,     pv.id_proveedor as idProveedor, \r\n"
				+ "				    pv.nombre_empresa AS nombreProveedor,  ub.pasillo, ub.estante  FROM productos p\r\n"
				+ "				INNER JOIN categorias c ON p.id_categoria = c.id_categoria \r\n"
				+ "				LEFT JOIN proveedor pv ON p.id_proveedor = pv.id_proveedor \r\n"
				+ "				left join ubicacion ub on p.id_ubicacion  = ub.id_ubicacion";
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
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public void guardar(Productos productos) {
		String sql = "INSERT INTO productos "
				+ "(codigo_barras, nombre, descripcion, id_categoria, unidad, precio_unitario, "
				+ "stock_actual, stock_minimo, id_ubicacion, activo, id_proveedor, imagenProducto) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setString(1, productos.getCodigoBarras());
			ps.setString(2, productos.getNombre());
			ps.setString(3, productos.getDescripcion());
			ps.setInt(4, productos.getIdCategoria());
			ps.setString(5, productos.getUnidad());
			ps.setDouble(6, productos.getPrecioUnitario());
			ps.setInt(7, productos.getStockActual());
			ps.setInt(8, productos.getStockMinimo());
			ps.setInt(9, productos.getIdUbicacion());
			ps.setBoolean(10, productos.isActivo());
			ps.setInt(11, productos.getIdProveedor());
			ps.setBytes(12, productos.getArchivo());
			ps.executeUpdate();

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void actualizar(Productos productos) {

		String sql = "UPDATE productos SET " + "codigo_barras = ?, " + "nombre = ?, " + "descripcion = ?, "
				+ "id_categoria = ?, " + "unidad = ?, " + "precio_unitario = ?, " + "stock_actual = ?, "
				+ "stock_minimo = ?, " + "id_ubicacion = ?, " + "activo = ?, " + "id_proveedor = ? "
				+ "WHERE id_producto = ?";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setString(1, productos.getCodigoBarras());
			ps.setString(2, productos.getNombre());
			ps.setString(3, productos.getDescripcion());
			ps.setInt(4, productos.getIdCategoria());
			ps.setString(5, productos.getUnidad());
			ps.setDouble(6, productos.getPrecioUnitario());
			ps.setInt(7, productos.getStockActual());
			ps.setInt(8, productos.getStockMinimo());
			ps.setInt(9, productos.getIdUbicacion());
			ps.setBoolean(10, productos.isActivo());
			ps.setInt(11, productos.getIdProveedor());
			;
			ps.setInt(12, productos.getIdProducto());

			ps.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	public void actualizarStock(int idProducto, int stock) {

		String sql = "UPDATE productos SET stock_actual = ? " + "WHERE id_producto = ?";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setInt(1, stock);
			ps.setInt(2, idProducto);
			ps.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	public void eliminarProducto(int idProducto) {

		String sql = "DELETE FROM PRODUCTOS WHERE ID_PRODUCTO = ?";
		try (Connection connection = Conexion.getConexion();
				PreparedStatement statement = connection.prepareStatement(sql);) {

			statement.setInt(1, idProducto);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public int getByIdProducto(int idProducto) {
		String sql = "SELECT stock_actual FROM productos where id_producto = ?";
		int p = 0;
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				p = rs.getInt("stock_actual");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return p;
	}

	public Productos getByIdCodigoBarras(String codigoBarras) {
		String sql = "SELECT     p.*, c.id_categoria AS idCategoria, \r\n"
				+ "				     c.nombre AS nombreCategoria,     pv.id_proveedor as idProveedor, \r\n"
				+ "				    pv.nombre_empresa AS nombreProveedor,  ub.pasillo, ub.estante  FROM productos p\r\n"
				+ "				INNER JOIN categorias c ON p.id_categoria = c.id_categoria \r\n"
				+ "				LEFT JOIN proveedor pv ON p.id_proveedor = pv.id_proveedor \r\n"
				+ "				left join ubicacion ub on p.id_ubicacion  = ub.id_ubicacion where p.codigo_barras like CONCAT('%', ?, '%')";
		Productos p = new Productos();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, codigoBarras);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.mapRowBy(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			 logger.debug(e.getMessage());
		}
		return p;
	}

	public Productos getByIdProductoInfo(int idProducto) {
		String sql = "SELECT id_producto, nombre as nombreProducto, codigo_barras, descripcion, id_categoria, unidad, "
				+ "precio_unitario, stock_actual as stockActual, stock_minimo as stockMinimo, "
				+ "ubicacion, activo FROM productos where id_producto = ?";
		Productos p = new Productos();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.mapRowBy(rs);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return p;
	}

	public void bajaProducto(int idProducto) {
		String sql = "UPDATE productos  SET activo = 0  WHERE ID_PRODUCTO = ?";
		try (Connection connection = Conexion.getConexion();
				PreparedStatement statement = connection.prepareStatement(sql);) {

			statement.setInt(1, idProducto);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	public List<Productos> getStockBajo() {
		String sql = "SELECT id_producto, nombre, stock_actual, stock_minimo \r\n" + "FROM productos \r\n"
				+ "WHERE stock_actual <= stock_minimo \r\n" + "AND activo = 1;";
		List<Productos> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Productos p = new Productos();
				p = mapper.mapRowStockBajo(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public List<Productos> getProductosFaltantes() {
		String sql = "select * from productos where stock_actual = 0;";
		List<Productos> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Productos p = new Productos();
				p = mapper.mapRowSinExistencias(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public int getTotalStockBajo() {
		String sql = "select COUNT(*) as total from productos where stock_actual <= stock_minimo ";
		int numero = 0;
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				numero = rs.getInt("total");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return numero;
	}

	public Productos getByNombreProducto(String codigoBarras) {
		String sql = "SELECT p.*, \r\n"
				+ "       c.id_categoria AS idCategoria, \r\n"
				+ "       c.nombre AS nombreCategoria,     \r\n"
				+ "       pv.id_proveedor as idProveedor, \r\n"
				+ "       pv.nombre_empresa AS nombreProveedor,  \r\n"
				+ "       ub.pasillo, \r\n"
				+ "       ub.estante  \r\n"
				+ "FROM productos p\r\n"
				+ "INNER JOIN categorias c ON p.id_categoria = c.id_categoria \r\n"
				+ "LEFT JOIN proveedor pv ON p.id_proveedor = pv.id_proveedor \r\n"
				+ "LEFT JOIN ubicacion ub ON p.id_ubicacion = ub.id_ubicacion \r\n"
				+ "WHERE p.nombre LIKE CONCAT('%', ?, '%')";
		Productos p = new Productos();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, codigoBarras);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.mapRowBy(rs);
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return p;
	}
}
