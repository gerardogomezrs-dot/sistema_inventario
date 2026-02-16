package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import lombok.Data;

@Named("movimientosAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosAlmacenBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private boolean modoManual = false;

	public MovimientosAlmacenBean() {
			
	}

	@PostConstruct
	public void init() {

	}
}
