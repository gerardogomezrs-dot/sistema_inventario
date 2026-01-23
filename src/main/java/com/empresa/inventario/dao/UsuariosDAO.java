package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.UsuariosMapper;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.utils.Conexion;


public class UsuariosDAO {

	private UsuariosMapper mapper = new UsuariosMapper();

	public Usuario login(String username, String password) {

		Usuario usuario = null;

		try (Connection conn = Conexion.getConexion();
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE userName=? AND password=?")) {

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				usuario = new Usuario();
				usuario.setIdUsuario(rs.getInt("id_usuario"));
				usuario.setNombre(rs.getString("nombre"));
				usuario.setRol(rs.getString("rol"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}

	public List<Usuario> getAll() throws Exception {
		String sql = "SELECT * FROM usuarios";
		List<Usuario> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			// Usamos while para iterar sobre cada fila del ResultSet
			while (rs.next()) {
				Usuario p = new Usuario();
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

	public void guardar(Usuario usuario) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "INSERT INTO usuarios " + "(nombre, rol, permisos,userName, password, activo) " + "VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, usuario.getNombre());
		ps.setString(2, usuario.getRol());
		ps.setString(3, usuario.getPermisos());
		ps.setString(4, usuario.getUserName());
		ps.setString(5, usuario.getPassword());
		ps.setBoolean(6, usuario.isActivo());

		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

	public void actualizar(Usuario usuario) throws Exception {

		Connection conexion = Conexion.getConexion();

		String sql = "UPDATE usuarios SET " + "nombre = ?, " + "rol = ?, " + "permisos = ?, " + "userName = ?, " + "password = ?, "
				+ "activo = ? " + "WHERE id_usuario = ?";

		PreparedStatement ps = conexion.prepareStatement(sql);

		ps.setString(1, usuario.getNombre());
		ps.setString(2, usuario.getRol());
		ps.setString(3, usuario.getPermisos());
		ps.setString(4, usuario.getUserName());
		ps.setString(5, usuario.getPassword());
		ps.setBoolean(6, usuario.isActivo());
		ps.setInt(7, usuario.getIdUsuario());
		ps.executeUpdate();

		ps.close();
		conexion.close();
	}

	public void eliminarUsuario(int idUsuario) throws Exception {

		Connection connection = Conexion.getConexion();

		String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setInt(1, idUsuario);
		statement.executeUpdate();
		statement.close();
		connection.close();

	}

}
