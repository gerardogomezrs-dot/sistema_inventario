package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

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

	private UploadedFile uploadedFile;

	private List<Categorias> categoriasList;

	@Inject
	private ICategoriaService categoriaService;

	public CategoriasBean() {
		categorias = new Categorias();
	}

	@PostConstruct
	public void init() {
		try {
			listaCategorias();

			list = categoriaService.getAllCategorias();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void listaTabla() {

		if (this.categorias != null) {
			listaTablaCategorias.add(this.categorias);

			// Limpiar el objeto para el siguiente registro
			this.categorias = new Categorias();
		}

	}

	public void guardar() throws Exception {
		// 1. Guardar los datos
		if (listaTablaCategorias != null && !listaTablaCategorias.isEmpty()) {
			categoriaService.save(listaTablaCategorias);
		}

		// 2. Limpiar las listas en memoria para que la tabla se vea vacía
		if (listaTablaCategorias != null) {
			listaTablaCategorias.clear();
		}

		// 3. Reiniciar el objeto individual (el que está en el formulario)
		this.categorias = new Categorias(); // Asumiendo que tu objeto se llama 'categorias'

		// 4. Notificar al usuario
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Categoria guardada", "La categoria fue guardada correctamente"));
	}

	public void actualizar() throws Exception {
		if (categorias == null) {
			throw new ExceptionMessage("Ingresa datos");
		} else {
			categoriaService.update(categorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categoria actualizada", "La categoria fue actualizada correctamente"));
		}
	}

	public List<Categorias> listaCategorias() throws Exception {
		return list = categoriaService.getAllCategorias();
	}

	public void eliminarCategoria() throws Exception {
		categoriaService.delete(categorias.getIdCategoria());
		list = categoriaService.getAllCategorias(); // refresca la tabla
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Categoria eliminada", "La categoria fue eliminada correctamente"));
	}

	public void cargarArchivo() {
		if (uploadedFile == null || uploadedFile.getContents() == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
		}
		listaTablaCategorias = new ArrayList<Categorias>();
		listaTablaCategorias = categoriaService.cargarArchivo(uploadedFile);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
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
