package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReporteStockBajo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoBarras;
	private String nombreProducto;
	private String stockActual;
	private String stockMinimo;
	private String faltanteSugerido;
	private String categoria;

}
