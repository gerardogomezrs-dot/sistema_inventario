package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReportesExistencias implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nombreProducto;
	private String nombreCategoria;
	private int stockDisponible;
	private String ubicacion;
	private Double precioUnitario;
}
