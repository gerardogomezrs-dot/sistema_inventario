package com.empresa.inventario.service;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Categorias;

public interface ICategoriaService {
	
	void save(List<Categorias> e);
	void update(Categorias categorias);
	List<Categorias> getAllCategorias();
	void delete(int idCategoria);
	List<Categorias> cargarArchivo(UploadedFile file);
	List<Categorias> byNombreCategoria(String nombre);
}
