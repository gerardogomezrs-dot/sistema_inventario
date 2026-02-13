package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.CategoriaMapper;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.utils.Conexion;

public class CategoriasDAO {

	private CategoriaMapper mapper = new CategoriaMapper();

	public void guardar(Categorias e) throws Exception {
		String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setString(1, e.getNombre());
			ps.setString(2, e.getDescripcion());

			ps.executeUpdate();

		} catch (SQLException ex) {
			System.err.println("Error al insertar categoría: " + ex.getMessage());
			throw ex;
		}
	}

	public void actualizar(Categorias e) throws Exception {
		String sql = "UPDATE categorias SET " + "nombre = ?, " + "descripcion = ? " + " WHERE id_categoria = ?";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getNombre());
			ps.setString(2, e.getDescripcion());
			ps.setInt(3, e.getIdCategoria());
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public List<Categorias> getAllCategorias() throws Exception {
		String sql = "SELECT * FROM categorias";
		List<Categorias> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Categorias p = new Categorias();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public void eliminarCategoria(int idCategoria) throws Exception {
		Connection connection = Conexion.getConexion();
		String sql = "DELETE FROM CATEGORIAS WHERE ID_CATEGORIA = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, idCategoria);
		statement.executeUpdate();
		statement.close();
		connection.close();
	}
}
