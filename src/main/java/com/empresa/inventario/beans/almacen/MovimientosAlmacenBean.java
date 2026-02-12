package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Named("movimientosAlmacenBean") 
@javax.faces.view.ViewScoped
@Data
public class MovimientosAlmacenBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(MovimientosAlmacenBean.class);
	
	private boolean modoManual = false; 

	
	public MovimientosAlmacenBean() {
		
	}
	
	@PostConstruct
	public void init() {
		
	}

}
