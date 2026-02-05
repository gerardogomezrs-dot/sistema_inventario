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

import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("productoBean") // Nombre para usar en el XHTML
@javax.faces.view.ViewScoped
@Data
public class ProductoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean modoManual = false; // Inicia en modo escáner (oculto)

	private List<Categorias> listaCategorias;

	private List<Productos> listaProductosGuardar = new ArrayList<Productos>();

	private List<Productos> list;

	private UploadedFile uploadedFile;

	private Productos producto;

	@Inject
	private transient IProductoService iProductoService;

	@Inject
	private transient ICategoriaService iCategoriaService;

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
			iProductoService.update(producto); // UPDATE real
			ListaProductos(); // refresca tabla
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void eliminar() throws Exception {
		try {
			iProductoService.delete(producto.getIdProducto()); // o DAO
			list = iProductoService.getAll(); // refresca la tabla
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Producto eliminado", "El producto fue eliminado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ListaProductos() throws Exception {

		this.list = iProductoService.getAll();

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

	public String getIndex() {
		return "/pages/admin/productos/tablaProductos?faces-redirect=true";
	}

	public String menuPrincipal() {
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		return "/pages/admin/productos/productos.xhtml?faces-redirect=true";
	}

}
