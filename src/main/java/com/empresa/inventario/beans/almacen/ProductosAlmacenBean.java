package com.empresa.inventario.beans.almacen;

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
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("productosAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class ProductosAlmacenBean implements Serializable {
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

	public ProductosAlmacenBean() {
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

	public void ListaProductos() throws Exception {
		this.list = iProductoService.getAll();
	}

	public String irATablaProductos() {
		return "/pages/almacen/productos/tablaProductos?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/almacen/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		return "/pages/almacen/productos/producto?faces-redirect=true";
	}

	public void guardarTabla() {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();
	}

	public void guardarProductoTabla() {
		List<Productos> listaProductos = listaProductosGuardar;
		try {
			iProductoService.create(listaProductos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.listaProductosGuardar.clear();
		this.producto = new Productos();
	}

	public void actualizar() {
		try {
			iProductoService.update(producto);
			ListaProductos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			iProductoService.delete(producto.getIdProducto());
			list = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto eliminado", "El producto fue dado de correctamente"));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cargaArchivos() throws Exception{		
			if(uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<Productos>();
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
	}
	

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
