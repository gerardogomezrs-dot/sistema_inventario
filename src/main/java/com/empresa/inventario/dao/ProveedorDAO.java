package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.empresa.inventario.mapper.ProveedorMapper;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.utils.Conexion;

public class ProveedorDAO {

	private ProveedorMapper mapper = new ProveedorMapper();

	public List<Proveedor> getAll() throws Exception {
		String sql = "SELECT * from proveedor";
		List<Proveedor> lista = new ArrayList<>();
		try (Connection con = Conexion.getConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Proveedor p = new Proveedor();
				p = mapper.rowtoMap(rs);
				lista.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return lista;
	}

	public void guardar(Proveedor e) throws Exception {
		String sql = "INSERT INTO proveedor (nombre_empresa, "
				+ "contacto_nombre, telefono, email, direccion, activo, fecha_registro) "
				+ "VALUES(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getNombreEmpresa());
			ps.setString(2, e.getContactoEmpresa());
			ps.setString(3, e.getTelefono());
			ps.setString(4, e.getEmail());
			ps.setString(5, e.getDireccion());
			ps.setBoolean(6, e.isActivo());
			ps.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Error al insertar categoría: " + ex.getMessage());
			throw ex;
		}
	}

	public void eliminarProveedor(int idProveedor) throws Exception {
		System.out.println("Eliminado proveedor");
		String sql = "DELETE FROM PROVEEDOR WHERE ID_PROVEEDOR = ?";
		try (Connection connection = Conexion.getConexion();

				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, idProveedor);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Proveedor e) throws Exception {
		String sql = "UPDATE proveedor SET " + "nombre_empresa = ?, "
				+ "contacto_nombre = ?, telefono = ?, email = ?, direccion = ?, activo = ? WHERE id_proveedor = ? ";
		try (Connection conexion = Conexion.getConexion(); PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setString(1, e.getNombreEmpresa());
			ps.setString(2, e.getContactoEmpresa());
			ps.setString(3, e.getTelefono());
			ps.setString(4, e.getEmail());
			ps.setString(5, e.getDireccion());
			ps.setBoolean(6, e.isActivo());
			ps.setInt(7, e.getIdProveedor());
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

}