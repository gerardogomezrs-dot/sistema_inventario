package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReporteStockBajoAlmacen {
	
	private String codigoBarras;
	private String nombreProducto;
	private int stockActual;
	private int stockPermitido;
	private int faltanteReorden;
}
