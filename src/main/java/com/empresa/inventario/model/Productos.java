package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Productos implements Serializable{

	
	 
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
	private int stockActual;
	private int stockMinimo;
	private String ubicacion;
	private boolean activo;

}
