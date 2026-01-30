package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.CategoriasDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Categorias;

@Named("categoriaService")
@ApplicationScoped
public class CategoriaServiceImpl implements ICategoriaService{
	
	private CategoriasDAO dao = new CategoriasDAO();
	
	@Override
	public void save(List<Categorias> list) throws Exception {
		for(Categorias categorias: list) {
		if(categorias==null) {
			throw new ExceptionMessage("Vacio");
		} else {
			dao.guardar(categorias);
		}
		}
	}

	@Override
	public void update(Categorias categorias) throws Exception {
		if(categorias==null) {
			throw new ExceptionMessage("Vacio");
		} else {
			dao.actualizar(categorias);
		}
		
	}

	@Override
	public List<Categorias> getAllCategorias() throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		categorias = dao.getAllCategorias();
		try {
		if(categorias.size()==0) {
			throw new ExceptionMessage("Lista Vacia");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return categorias;
	}

	@Override
	public void delete(int idCategoria) throws Exception {
		if(idCategoria ==0) {
			throw new ExceptionMessage("Ingrese el id");
		}else {
			dao.eliminarCategoria(idCategoria);
		}
	}

}
