package com.empresa.inventario.service;

import java.util.List;
import java.util.function.Consumer;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Categorias;

public interface ICategoriaService {
	
	void save(List<Categorias> e, Consumer<Integer> progresoCallback);
	void update(Categorias categorias);
	List<Categorias> getAllCategorias();
	void delete(int idCategoria);
	List<Categorias> cargarArchivo(UploadedFile file);

}
