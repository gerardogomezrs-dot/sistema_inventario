package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReporteClasificacionABC implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreProducto;
	private Double valorStock;
	private String clasificacionABC;
}
