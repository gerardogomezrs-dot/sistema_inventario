package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Categorias implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Categorias() {
		super();
	}
	
	private int idCategoria;
	private String nombre;
	private String descripcion;
}
