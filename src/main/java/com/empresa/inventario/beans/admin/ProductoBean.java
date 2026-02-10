package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.sql.SQLException;
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
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("productoBean")
@javax.faces.view.ViewScoped
@Data
public class ProductoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean modoManual = false;

	private List<Categorias> listaCategorias;

	private List<Productos> listaProductosGuardar = new ArrayList<Productos>();

	private List<Productos> list;

	private UploadedFile uploadedFile;

	private Productos producto;

	@Inject
	private IProductoService iProductoService;

	@Inject
	private ICategoriaService iCategoriaService;

	public ProductoBean() {
		producto = new Productos();
	}

	@PostConstruct
	public void init() {
		try {
			ListaProductos();
			listaCategorias = iCategoriaService.getAllCategorias();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();

	}

	public void guardarProductoTabla() throws Exception {
		List<Productos> listaProductos = listaProductosGuardar;
		iProductoService.create(listaProductos);
		this.listaProductosGuardar.clear();
		this.producto = new Productos();
	}

	public void guardarCambios() throws Exception {
		try {
			iProductoService.update(producto);
			ListaProductos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			iProductoService.delete(producto.getIdProducto());
			list = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto eliminado", "El producto fue eliminado correctamente"));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ListaProductos() throws Exception {
		this.list = iProductoService.getAll();
	}

	public void cargaArchivos() throws Exception {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<Productos>();
			System.out.println("Nombre archivo: " + uploadedFile.getFileName());
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		}
	}

	public String getIndex() {
		return "/pages/admin/productos/tablaProductos?faces-redirect=true";
	}

	public String menuPrincipal() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		return "/pages/admin/productos/productos.xhtml?faces-redirect=true";
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
