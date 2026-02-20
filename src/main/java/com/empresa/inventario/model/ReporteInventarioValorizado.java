package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReporteInventarioValorizado {

	private String codigoBarras;
	private String nombreProducto;
	private String categoria;
	private String ubicacion;
	private String stockActual;
	private String unidad;
	private Double precioUnitario;
	private int precioTotal;
	
}
