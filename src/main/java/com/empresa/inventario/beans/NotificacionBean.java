package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.Productos;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("notificacionBean")
@javax.faces.view.ViewScoped
@Data
public class NotificacionBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient IProductoService iProductoService;
	
	private transient List<Productos> listaProductosCriticos;
	
	private int cantidadCriticos;
	
	private boolean mostrarAlerta = true; // Variable de control
	
	@Inject
	public NotificacionBean(IProductoService iProductoService) {
		this.iProductoService = iProductoService;
	}
	
	@PostConstruct
	public void init() {
		cargarAlertas();
		this.mostrarAlerta = true;
		
	}
	
	public void cargarAlertas() {
		this.listaProductosCriticos = iProductoService.getStockBajo();
		this.cantidadCriticos = listaProductosCriticos.size();
	}
	
	public void ocultarAlerta() {
        this.mostrarAlerta = false;
    }
	

	
}
