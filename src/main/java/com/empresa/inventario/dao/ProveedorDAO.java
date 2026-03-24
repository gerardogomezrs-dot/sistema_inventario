package com.empresa.inventario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.mapper.ProveedorMapper;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.utils.Conexion;

public class ProveedorDAO{

	private ProveedorMapper mapper = new ProveedorMapper();
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProveedorDAO.class);


	public List<Proveedor> getAll() {
		String sql = "SELECT p.* from proveedor p";
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
			logger.debug(e.getMessage());
		}
		return lista;
	}

	public void guardar(Proveedor e) {
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
			logger.debug(ex.getMessage());
		}
	}

	public void eliminarProveedor(int idProveedor) {
		String sql = "DELETE FROM PROVEEDOR WHERE ID_PROVEEDOR = ?";
		try (Connection connection = Conexion.getConexion();

				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setInt(1, idProveedor);
			statement.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void update(Proveedor e) {
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
			logger.debug(ex.getMessage());
		
		}
	}
	
	public List<Proveedor> getByNombreEmpresa(String nombreEmpresa) {
		String sql = "SELECT p.* from proveedor p where nombre_empresa like CONCAT('%', ?, '%');";
		List<Proveedor> proveedors = new ArrayList<>();
		Proveedor p = new Proveedor();
		try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setString(1, nombreEmpresa);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				p = mapper.rowtoMap(rs);
				proveedors.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return proveedors;
	}
	
}