package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReporteMermasDevolucion {
	
	private Date fecha;
	private String nombreProducto;
	private String tipo;
	private String responsable;
	private String rol;
	private int cantidad;
	private int totalPerdido;

}
