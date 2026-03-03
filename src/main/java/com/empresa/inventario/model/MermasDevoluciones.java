package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class MermasDevoluciones {
	
	private int idMerma;
	private Productos productos;
	private int cantidad;
	private String motivo;
	private Date fecha;
	private Double costoPerdido;

}
