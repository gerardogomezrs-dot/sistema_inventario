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
	
	private CategoriaMapper mapper = new CategoriaMapper();;

	public CategoriasDAO() {

	}

	
	public void guardar(Categorias e) throws Exception {
		Connection conexion = Conexion.getConexion();

		String sql = "INSERT INTO categorias " + "(nombre, descripcion) " + "VALUES (?, ?)";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, e.getNombre());
		ps.setString(2, e.getDescripcion());
		ps.executeUpdate();

		ps.close();
		conexion.close();
	}
	
	public void actualizar(Categorias e) throws Exception {
		Connection conexion = Conexion.getConexion();

		String sql = "UPDATE categorias SET " + "nombre = ?, " + "descripcion = ? "
				+ " WHERE id_categoria = ?";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, e.getNombre());
		ps.setString(2, e.getDescripcion());
		ps.setInt(3, e.getIdCategoria());
		ps.executeUpdate();

		ps.close();
		conexion.close();
	}
	
	public List<Categorias> getAllCategorias() throws Exception{
		String sql = "SELECT * FROM categorias";
		List<Categorias> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			// Usamos while para iterar sobre cada fila del ResultSet
			while (rs.next()) {
				Categorias p = new Categorias();
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
