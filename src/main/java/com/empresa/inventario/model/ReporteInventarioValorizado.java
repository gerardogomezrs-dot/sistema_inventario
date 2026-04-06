package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReporteInventarioValorizado implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoBarras;
	private String nombreProducto;
	private String categoria;
	private String ubicacion;
	private String stockActual;
	private String unidad;
	private Double precioUnitario;
	private int precioTotal;
	
}
