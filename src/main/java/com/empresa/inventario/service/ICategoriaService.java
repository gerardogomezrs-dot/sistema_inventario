package com.empresa.inventario.service;

import java.util.List;
import java.util.function.Consumer;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Categorias;

public interface ICategoriaService {
	
	void save(List<Categorias> e, Consumer<Integer> progresoCallback) throws Exception;
	void update(Categorias categorias) throws Exception;
	List<Categorias> getAllCategorias() throws Exception;
	void delete(int idCategoria)  throws Exception;
	List<Categorias> cargarArchivo(UploadedFile file);

}
