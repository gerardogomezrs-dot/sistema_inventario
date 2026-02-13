package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Proveedor;

public class ProveedorMapper {

	
	public Proveedor rowtoMap(ResultSet resultSet) throws SQLException {
		Proveedor proveedor = new Proveedor();
		proveedor.setIdProveedor(resultSet.getInt("id_proveedor"));
		proveedor.setNombreEmpresa(resultSet.getString("nombre_empresa"));
		proveedor.setContactoEmpresa(resultSet.getString("contacto_nombre"));
		proveedor.setTelefono(resultSet.getString("telefono"));
		proveedor.setEmail(resultSet.getString("email"));
		proveedor.setDireccion(resultSet.getString("direccion"));
		proveedor.setActivo(resultSet.getBoolean("activo"));
		proveedor.setFechaRegistro(resultSet.getDate("fecha_registro"));
		return proveedor;
	}
	
}
