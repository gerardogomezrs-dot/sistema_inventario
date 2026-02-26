package com.empresa.inventario.service;

import java.util.List;
import java.util.function.IntConsumer;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Productos;

public interface IProductoService {

	List<Productos> create(List<Productos> productos, IntConsumer progresoCallback);

	void delete(int idProducto);
	
	void bajaProducto(int idProducto);

	void update(Productos productos) ;

	List<Productos> getAll();

	List<Productos> cargaArchivos(UploadedFile uploadedFile) ;
	
	Productos getByCodigoBarras(String codigoBarras);
	}
