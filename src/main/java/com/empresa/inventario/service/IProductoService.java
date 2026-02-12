package com.empresa.inventario.service;

import java.util.List;
import java.util.function.Consumer;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Productos;

public interface IProductoService {

	List<Productos> create(List<Productos> productos, Consumer<Integer> progresoCallback) throws Exception;

	void delete(int idProducto) throws Exception;
<<<<<<< HEAD
	
	void bajaProducto(int idProducto) throws Exception;
=======
<<<<<<< HEAD
	
	void bajaProducto(int idProducto) throws Exception;
=======
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d

	void update(Productos productos) throws Exception;

	List<Productos> getAll() throws Exception;

	List<Productos> cargaArchivos(UploadedFile uploadedFile) throws Exception;
	
	Productos getByCodigoBarras(String codigoBarras) throws Exception;
	}
