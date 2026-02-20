package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReporteAuditoriaUsuario {
	private String usuarioResponsable;
	private Date fechaHora;
	private String nombreProducto;
	private String tipoMovimiento;
	private int cantidad;
	private String observaciones;
}
