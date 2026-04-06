package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ReporteAuditoriaUsuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuarioResponsable;
	private Date fechaHora;
	private String nombreProducto;
	private String tipoMovimiento;
	private int cantidad;
	private String observaciones;
}
