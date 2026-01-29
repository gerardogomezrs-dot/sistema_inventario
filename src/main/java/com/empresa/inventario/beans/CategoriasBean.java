package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.service.ICategoriaService;

import lombok.Data;

@Named("categoriasBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasBean implements Serializable {

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	
	private List<Categorias> filteredList; // Lista para almacenar los resultados filtrados

	
	private List<Categorias> list;

	private Categorias categorias;
	
	private List<Categorias> listaTablaCategorias = new ArrayList<Categorias>();

	@Inject
	private  ICategoriaService categoriaService;

	public CategoriasBean() {
		categorias = new Categorias();
	}

	@PostConstruct
	public void init() {
		try {
			//listaCategorias();
			
			list = categoriaService.getAllCategorias();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void listaTabla() {
	    
	    if (this.categorias != null) {
	        listaTablaCategorias.add(this.categorias);
	        System.out.println("Tamaño lista: " + listaTablaCategorias.size());
	        
	        // Limpiar el objeto para el siguiente registro
	        this.categorias = new Categorias();
	    }
		
	}
	
	public void guardar() throws Exception {
			categoriaService.save(listaTablaCategorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categoria guardada", "La categoria fue guardada correctamente"));	
	}
	

	public void actualizar() throws Exception {
		if(categorias==null) {
			throw new ExceptionMessage("Ingresa datos");
		}else {
			categoriaService.update(categorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categoria actualizada", "La categoria fue actualizada correctamente"));
		}
	}
	
	public List<Categorias> listaCategorias() throws Exception{
		return list = categoriaService.getAllCategorias();
	}
	
	public void eliminarCategoria() throws Exception {
		categoriaService.delete(categorias.getIdCategoria());
		list = categoriaService.getAllCategorias(); // refresca la tabla
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Categoria eliminada", "La categoria fue eliminada correctamente"));
	}
	
	
	public String irANuevaCategoria() {
		return "/pages/admin/categorias/categorias.xhtml?faces-redirect=true";
	}
	
	public String irATablaCategoria() {
		return "/pages/admin/categorias/tablaCategorias.xhtml?faces-redirect=true";
		
	}
	
	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

}
