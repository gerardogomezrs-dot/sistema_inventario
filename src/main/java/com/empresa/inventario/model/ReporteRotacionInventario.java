package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReporteRotacionInventario {
	
	private String nombreProducto;
	private int totalUnidadesSalida;
	private int stockActual;
	private Double indiceRotacion;

}
