package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.UsuariosMapper;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.utils.Conexion;
import com.empresa.inventario.utils.PasswordUtil;

public class UsuariosDAO {

	private UsuariosMapper mapper = new UsuariosMapper();

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuariosDAO.class);

	public Usuario login(String userName) {

		Usuario p = new Usuario();
		String sql = "SELECT id_usuario, nombre, rol, permisos, user_name, password, activo FROM usuarios WHERE user_name = ? ";

		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, userName);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					p = mapper.mapRow(rs);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return p;
	}

	public Usuario getById(int idUsuario) {
		Usuario p = new Usuario();
		String sql = "SELECT id_usuario, nombre, rol, permisos, user_name, password, activo FROM usuarios  WHERE id_usuario = ? ";

		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idUsuario);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					p = mapper.mapRow(rs);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return p;
	}

	public String validarUserName(String userName) {

		String userNameValidar = "";
		String sql = "SELECT user_name FROM usuarios WHERE user_name = ? ";

		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, userName);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					userNameValidar = mapper.mapRowUserName(rs);
				}
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return userNameValidar;
	}

	public List<Usuario> getAll() {
		String sql = "SELECT id_usuario, nombre, rol, permisos, user_name, password, activo FROM usuarios";
		List<Usuario> lista = new ArrayList<>();

		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Usuario p = new Usuario();
				p = mapper.mapRow(rs);
				lista.add(p);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());

		}
		return lista;
	}

	public void guardar(Usuario usuario) {

		String sql = "INSERT INTO usuarios " + "(nombre, rol, permisos,user_name, password, activo) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getRol());
			ps.setString(3, usuario.getPermisos());
			ps.setString(4, usuario.getUserName());
			ps.setString(5, PasswordUtil.encrypt(usuario.getPassword()));
			ps.setBoolean(6, usuario.isActivo());

			ps.executeUpdate();

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void actualizar(Usuario usuario) {

		String sql = "UPDATE usuarios SET " + "nombre = ?, " + "rol = ?, " + "permisos = ?, " + "user_name = ?, "
				+ "password = ?, " + "activo = ? " + "WHERE id_usuario = ?";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getRol());
			ps.setString(3, usuario.getPermisos());
			ps.setString(4, usuario.getUserName());
			ps.setString(5, PasswordUtil.encrypt(usuario.getPassword()));
			ps.setBoolean(6, usuario.isActivo());
			ps.setInt(7, usuario.getIdUsuario());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void eliminarUsuario(int idUsuario) {

		String sql = "DELETE FROM USUARIOS WHERE ID_USUARIO = ?";
		try (Connection connection = Conexion.getConexion();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, idUsuario);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void actualizarPerfilUsuario(Usuario usuario) {

		String sql = "UPDATE usuarios SET " + "nombre = ?,  user_name = ?, " + "password = ?, " + "activo = ? "
				+ "WHERE id_usuario = ?";

		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql);) {

			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getUserName());
			ps.setString(3, PasswordUtil.encrypt(usuario.getPassword()));
			ps.setBoolean(4, usuario.isActivo());
			ps.setInt(5, usuario.getIdUsuario());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

}
