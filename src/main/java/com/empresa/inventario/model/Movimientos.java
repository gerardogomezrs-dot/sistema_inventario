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
	
	private String ubicacion;
	
	private String nombreProducto;
	
	private int productoExistencias;
	
	private String tipoMovimiento;
	
	private int cantidad;
	
	private Date fechaHora;
	
	private String origenDestino;
	
	private int idUsuario;
	
	private String observaciones;
	
	private int stockPrevio;
	
	private int stockPosterior;

	private Usuario usuario;

	private Productos productos;
	
	private String codigoBarras;
	
	private byte[] imagenProducto;
}
