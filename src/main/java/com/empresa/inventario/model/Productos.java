package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Productos implements Serializable{
	
	public Productos(){
	super();
	}
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idProducto;
	private String nombre;
	private String codigoBarras;
	private String descripcion;
	private int	idCategoria;
	private String unidad;
	private double precioUnitario;
	private int stockActual;
	private int stockMinimo;
	private String ubicacion;
	private boolean activo;

	private Categorias categorias;
	
	
}
