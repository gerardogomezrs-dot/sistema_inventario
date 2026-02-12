package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.sql.SQLException;
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
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

<<<<<<< HEAD
@Named("productoBean")
=======
<<<<<<< HEAD
@Named("productoBean")
=======
@Named("productoBean") // Nombre para usar en el XHTML
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
@javax.faces.view.ViewScoped
@Data
public class ProductoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
=======
<<<<<<< HEAD
	private boolean modoManual = false;
=======
	private boolean modoManual = false; // Inicia en modo escáner (oculto)
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e

>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
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

	public void guardarTabla() {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();
	}

	public void guardarProductoTabla() throws Exception {
		this.progreso = 0;
		List<Productos> listaProductos = listaProductosGuardar;

		List<Productos> copiar = new ArrayList<Productos>(listaProductos);
		if(copiar.isEmpty()) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			try {
				iProductoService.create(copiar, (valor) ->{
					this.progreso = valor;
				});
			}catch (Exception e) {
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


	public void guardarCambios() throws Exception {
<<<<<<< HEAD
		try {		
			iProductoService.update(producto);
			ListaProductos();
=======
		try {
<<<<<<< HEAD
			iProductoService.update(producto);
			ListaProductos();
=======
			iProductoService.update(producto); // UPDATE real
			ListaProductos(); // refresca tabla
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

<<<<<<< HEAD
	public void eliminar() {
=======
<<<<<<< HEAD
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
=======
	public void eliminar() throws Exception {
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
		try {
			iProductoService.delete(producto.getIdProducto());
			list = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto eliminado", "El producto fue eliminado correctamente"));
<<<<<<< HEAD
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
		}
		catch (Exception e) {
=======
		} catch (SQLException e) {
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
			e.printStackTrace();
		}
	}

	public void ListaProductos() throws Exception {
		this.list = iProductoService.getAll();
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======

>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
	}

	public void cargaArchivos() throws Exception {
		try {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<Productos>();
			System.out.println("Nombre archivo: " + uploadedFile.getFileName());
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
<<<<<<< HEAD
=======
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		}
=======
		if (uploadedFile == null || uploadedFile.getContents() == null) {
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		}
<<<<<<< HEAD
=======
		listaProductosGuardar = new ArrayList<Productos>();
		listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
	}catch (ExceptionMessage e) {
		añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
	}
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
	}

	public String irATablaProductos() {
		return "/pages/admin/productos/tablaProductos?faces-redirect=true";
	}

	public String menuPrincipal() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		return "/pages/admin/productos/productos.xhtml?faces-redirect=true";
	}
<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
	
>>>>>>> 5affef339816ef2c5228384dfb57cca732b4a05e
>>>>>>> 3d3eb6255d4bd97c1c31234f83079587041eaf8d
	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
