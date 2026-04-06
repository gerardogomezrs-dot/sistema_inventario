package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReporteRotacionInventario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreProducto;
	private int totalUnidadesSalida;
	private int stockActual;
	private Double indiceRotacion;

}
