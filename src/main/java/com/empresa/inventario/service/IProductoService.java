package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Productos;

public interface IProductoService {
	
	void create(Productos productos) throws Exception;
	void delete(int idProducto) throws Exception;
	void update(Productos productos) throws Exception;
	 List<Productos> getAll() throws Exception;

}
