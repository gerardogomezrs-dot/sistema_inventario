package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class Movimientos {

	/**
	 * 
	 */
	private int idMovimiento;
	private int idProducto;
	private String tipoMovimiento;
	private int cantidad;
	private Date fechaHora;
	private String origenDestino;
	private int idUsuario;
	private String observaciones;

	private Usuario usuario;

	private Productos productos;
	
	private String codigoBarras;
}
