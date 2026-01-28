package com.empresa.inventario.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.empresa.inventario.model.Categorias;

public class CategoriaMapper {
	
	public CategoriaMapper() {}
	
	public Categorias mapRow(ResultSet resultSet) throws SQLException{
		Categorias categorias = new Categorias();
		categorias.setIdCategoria(resultSet.getInt("id_categoria"));
		categorias.setNombre(resultSet.getString("nombre"));
		categorias.setDescripcion(resultSet.getString("descripcion"));
		return categorias;
	}

}
