package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Ubicacion;

public class UbicacionMapper {
	
	public Ubicacion getRow(ResultSet resultSet){
		Ubicacion ubicacion = new Ubicacion();
		try {
			ubicacion.setIdUbicacion(resultSet.getInt("id_ubicacion"));
			ubicacion.setPasillo(resultSet.getString("pasillo"));
			ubicacion.setEstante(resultSet.getString("estante"));
			ubicacion.setNivel(resultSet.getString("nivel"));
			ubicacion.setCodigoControl(resultSet.getString("codigo_control"));
			ubicacion.setEstado(resultSet.getString("estado"));
			} catch (SQLException e) {
			e.printStackTrace();
		}
		return ubicacion;
	}

}
