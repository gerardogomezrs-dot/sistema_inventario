package com.empresa.inventario.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("productoBean") // Nombre para usar en el XHTML
@ViewScoped
@Data
public class ProductoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Productos> list;

	private Productos producto;

	@Inject
	private IProductoService iProductoService;

	public ProductoBean() {
		producto = new Productos();

	}

	@PostConstruct
	public void init() {
		try {
			ListaProductos();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {

		try {
			if (producto == null) {
				throw new ExceptionMessage("Vacio ");
			} else {
				iProductoService.create(producto);
			}
		} catch (Exception e) {

			e.getMessage();
		}

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

	public String getIndex() {
		return "/pages/productos/tablaProductos?faces-redirect=true";
	}

	public String menuPrincipal() {
		return "/dashboard?faces-redirect=true";
	}



	public String irANuevoProducto() {
		return "/pages/productos/productos.xhtml?faces-redirect=true";
	}

}
