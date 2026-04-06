package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ReporteMermasDevolucion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date fecha;
	private String nombreProducto;
	private String tipo;
	private String responsable;
	private String rol;
	private int cantidad;
	private int totalPerdido;

}
