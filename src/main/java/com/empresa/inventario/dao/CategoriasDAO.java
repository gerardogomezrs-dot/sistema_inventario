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

	private CategoriaMapper mapper;

	public void guardar(Categorias e)  {
		String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {

			ps.setString(1, e.getNombre());
			ps.setString(2, e.getDescripcion());

			ps.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		
		}
	}

	public void actualizar(Categorias e)  {
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

	public List<Categorias> getAllCategorias() {
		String sql = "SELECT c.* FROM categorias c";
		List<Categorias> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Categorias p = new Categorias();
				mapper = new CategoriaMapper();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return lista;
	}

	public void eliminarCategoria(int idCategoria)  {
		String sql = "DELETE FROM CATEGORIAS WHERE ID_CATEGORIA = ?";
		try (Connection connection = Conexion.getConexion();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, idCategoria);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
