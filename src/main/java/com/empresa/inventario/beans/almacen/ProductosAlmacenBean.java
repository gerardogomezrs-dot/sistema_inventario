package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
<<<<<<< HEAD
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
=======
<<<<<<< HEAD
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
=======
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

<<<<<<< HEAD
import com.empresa.inventario.exceptions.ExceptionMessage;
=======
<<<<<<< HEAD
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
=======
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
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
	private List<Categorias> listaCategorias;
	private List<Productos> listaProductosGuardar = new ArrayList<Productos>();
	private List<Productos> list;
	private UploadedFile uploadedFile;
	private Productos producto;
	private Integer progreso = 0;

	@Inject
	private IProductoService iProductoService;

	@Inject
	private ICategoriaService iCategoriaService;

	public ProductosAlmacenBean() {
		producto = new Productos();
	}

	@PostConstruct
	public void init() {
<<<<<<< HEAD
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
		this.progreso = 0;
		List<Productos> listaProductos = listaProductosGuardar;

		List<Productos> copiar = new ArrayList<Productos>(listaProductos);
		if (copiar.isEmpty()) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			try {
				iProductoService.create(copiar, (valor) -> {
					this.progreso = valor;
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		this.listaProductosGuardar.clear();
		this.producto = new Productos();
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Categoria guardada", "El producto fue guardado correctamente"));
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

	public void cargaArchivos() throws Exception {
		if (uploadedFile == null || uploadedFile.getContents() == null) {
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
=======
		
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
	}

}
