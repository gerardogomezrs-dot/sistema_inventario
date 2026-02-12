package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

@Named("categoriasAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasAlmacenBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Categorias> filteredList;

	private List<Categorias> list;

	private Categorias categorias;

	private List<Categorias> listaTablaCategorias = new ArrayList<Categorias>();

	private UploadedFile uploadedFile;

	private List<Categorias> categoriasList;
	
	private Integer progreso = 0;


	@Inject
	private ICategoriaService categoriaService;

	public CategoriasAlmacenBean() {
		categorias = new Categorias();
	}

	@PostConstruct
	public void init() {
		try {
			listCategorias();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irANuevaCategoria() {
		return "/pages/almacen/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		return "/pages/almacen/categorias/consultaCategorias.xhtml?faces-redirect=true";
	}

	public void guardarCategoriasTabla() {
		if (this.categorias != null) {
			listaTablaCategorias.add(categorias);
			this.categorias = new Categorias();
<<<<<<< HEAD
		} else {
=======
<<<<<<< HEAD
		} else {
=======
		}else {
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
			throw new ExceptionMessage("Ingresa valores");
		}
	}

	public void guardar() throws Exception {
		progreso = 0;
		if (listaTablaCategorias != null && !listaTablaCategorias.isEmpty()) {
			this.progreso = 0;
			List<Categorias> copiaParaGuardar = new ArrayList<>(this.listaTablaCategorias);

			if (copiaParaGuardar.isEmpty()) {
				return;
			}
			CompletableFuture.runAsync(() -> {
				try {
					categoriaService.save(copiaParaGuardar, (valor) -> {
						this.progreso = valor;
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		if (listaTablaCategorias != null) {
			listaTablaCategorias.clear();
		}
		this.categorias = new Categorias();


	}

	public List<Categorias> listCategorias() throws Exception {
		return list = categoriaService.getAllCategorias();
	}

	public void actualizar() throws Exception {
		if (categorias != null) {
			categoriaService.update(categorias);
			categoriaService.update(categorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categoria actualizada", "La categoria fue actualizada correctamente"));
		}
	}

<<<<<<< HEAD
	public void eliminarCategoria() {
=======
<<<<<<< HEAD
	public void eliminarCategoria() {
=======
	public void eliminarCategoria() throws Exception {
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
		try {
			if (categorias != null) {
				categoriaService.delete(categorias.getIdCategoria());
				list = categoriaService.getAllCategorias();
<<<<<<< HEAD

=======
<<<<<<< HEAD

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Categoria eliminada", "La categoria fue eliminada correctamente"));
			} else {
				añadirMensaje(FacesMessage.SEVERITY_WARN, "Atención", "No se seleccionó ninguna categoría.");
			}

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();

		}

		catch (Exception e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();

		}
	}

	public void cargarArchivo() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}

			listaTablaCategorias = new ArrayList<Categorias>();
			listaTablaCategorias = categoriaService.cargarArchivo(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		}
	}

=======
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Categoria eliminada", "La categoria fue eliminada correctamente"));
			} else {
				añadirMensaje(FacesMessage.SEVERITY_WARN, "Atención", "No se seleccionó ninguna categoría.");
			}

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();

		}

		catch (Exception e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();

		}
	}

	public void cargarArchivo() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}

			listaTablaCategorias = new ArrayList<Categorias>();
			listaTablaCategorias = categoriaService.cargarArchivo(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		}
	}
<<<<<<< HEAD

=======
	
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
