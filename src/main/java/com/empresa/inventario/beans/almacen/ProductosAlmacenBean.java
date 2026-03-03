package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.IAuditoriaService;
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

	private IProductoService iProductoService;

	private List<Productos> productosList;
	
	private transient List<Productos> filteredProductosList;
	
	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public ProductosAlmacenBean(IProductoService iProductoService, IAuditoriaService auditoriaService) {
		this.iProductoService = iProductoService;
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		cargaListaProductos();
	}

	public void cargaListaProductos() {

		try {
			productosList = iProductoService.getAll();
			if (productosList.isEmpty()) {
				throw new ExceptionMessage("Lista Vacia");
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public String menuPrincipal() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/almacen/dashboard?faces-redirect=true";
	}

}
