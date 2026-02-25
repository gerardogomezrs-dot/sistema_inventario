package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReporteClasificacionABC {
	
	private String nombreProducto;
	private Double valorStock;
	private String clasificacionABC;
}
