package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;

import lombok.Data;

@Named("productosAlmacenBean") 
@javax.faces.view.ViewScoped
@Data
public class ProductosAlmacenBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean modoManual = false; 

	private List<Categorias> listaCategorias;

	private List<Productos> listaProductosGuardar = new ArrayList<Productos>();

	private List<Productos> list;

	private UploadedFile uploadedFile;

	private Productos producto;
	
	public ProductosAlmacenBean() {
		 producto = new Productos();		
	}
	
	@PostConstruct
	public void init() {
		
	}

}
