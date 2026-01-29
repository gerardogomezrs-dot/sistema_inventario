package com.empresa.inventario.model;

import java.sql.Date;

import lombok.Data;

@Data
public class ReportesMovimiento {

	
	private int idMovimiento;
	private Date fechaHora;
	private String codigoBarras;
	private String nombreProducto;
	private String categoria;
	private String tipoMovimiento;
	private String cantidad;
	private String usuarioResponsable;
}
