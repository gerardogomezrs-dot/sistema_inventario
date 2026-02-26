package com.empresa.inventario.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.CategoriaMapper;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.utils.Conexion;

public class CategoriasDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient CategoriaMapper mapper = new CategoriaMapper();

	public void guardar(Categorias e) {
		String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setString(1, e.getNombre());
			ps.setString(2, e.getDescripcion());

			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.getMessage();

		}
	}

	public void actualizar(Categorias e) {
		String sql = "UPDATE categorias SET " + "nombre = ?, " + "descripcion = ? " + " WHERE id_categoria = ?";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getNombre());
			ps.setString(2, e.getDescripcion());
			ps.setInt(3, e.getIdCategoria());
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.getMessage();
		}
	}

	public List<Categorias> getAllCategorias() {
		String sql = "SELECT c.* FROM categorias c";
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
			e.getMessage();
		}
		return lista;
	}

	public void eliminarCategoria(int idCategoria) {
		String sql = "DELETE FROM CATEGORIAS WHERE ID_CATEGORIA = ?";
		try (Connection connection = Conexion.getConexion();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, idCategoria);
			statement.executeUpdate();
		} catch (Exception e) {
			e.getMessage();
		}
	}

}
