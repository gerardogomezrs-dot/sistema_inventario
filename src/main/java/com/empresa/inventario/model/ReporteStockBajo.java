package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReporteStockBajo {
	
	private String codigoBarras;
	private String nombreProducto;
	private String stockActual;
	private String stockMinimo;
	private String faltanteSugerido;
	private String categoria;

}
