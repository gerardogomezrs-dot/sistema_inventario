package com.empresa.inventario.model;

import lombok.Data;

@Data
public class Categorias {

	public Categorias() {
		super();
	}
	
	private int idCategoria;
	private String nombre;
	private String descripcion;
}
