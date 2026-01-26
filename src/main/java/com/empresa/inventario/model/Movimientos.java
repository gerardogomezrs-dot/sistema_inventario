package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Movimientos implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
}
