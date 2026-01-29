package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Categorias;

public interface ICategoriaService {
	
	void save(List<Categorias> e) throws Exception;
	void update(Categorias categorias) throws Exception;
	List<Categorias> getAllCategorias() throws Exception;
	void delete(int idCategoria)  throws Exception;

}
