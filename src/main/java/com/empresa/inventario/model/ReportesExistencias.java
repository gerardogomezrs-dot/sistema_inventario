package com.empresa.inventario.model;

import lombok.Data;

@Data
public class ReportesExistencias {
	
	private String codigo;
	private String nombreProducto;
	private String nombreCategoria;
	private int stockDisponible;
	private String ubicacion;
	private Double precioUnitario;
}
