package com.empresa.inventario.model;

import lombok.Data;

@Data
public class Productos{
	 
	private int idProducto;
	private String nombre;
	private String codigoBarras;
	private String descripcion;
	private int idProveedor;
	private int	idCategoria;
	private String unidad;
	private double precioUnitario;
	private int stockActual;
	private int stockMinimo;
	private int idUbicacion;
	private boolean activo;
	private Categorias categorias;
	private Proveedor proveedor;
	private Ubicacion ubicacion;
	private byte[] archivo;
}
