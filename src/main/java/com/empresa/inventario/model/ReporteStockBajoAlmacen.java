package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReporteStockBajoAlmacen implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoBarras;
	private String nombreProducto;
	private int stockActual;
	private int stockPermitido;
	private int faltanteReorden;
}
