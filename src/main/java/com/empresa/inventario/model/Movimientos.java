package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class Movimientos {

	private int idMoviento;
	private int idProducto;
	private String tipo_Movimiento;
	private String cantidad;
	private Date fechaHora;
	private String origen_destino;
	private int idUsuario;
	private String observaciones;
	
}
