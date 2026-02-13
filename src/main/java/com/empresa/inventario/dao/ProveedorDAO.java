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
	
}
