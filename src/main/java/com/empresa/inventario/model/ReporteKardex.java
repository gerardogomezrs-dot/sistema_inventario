package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReporteKardex {

	private Date fecha;
	private String tipoMovimiento;
	private String concepto;
	private String origen;
	private	String entrada;
	private String salida;
	private String cantidadMovida;
	private String operador;
	
}
