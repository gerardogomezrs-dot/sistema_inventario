package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ReporteKardex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date fecha;
	private String tipoMovimiento;
	private String concepto;
	private String origen;
	private	String entrada;
	private String salida;
	private String cantidadMovida;
	private String operador;
	
}
