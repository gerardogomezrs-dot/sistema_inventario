package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class Proveedor {
	
	private int idProveedor;
	private String nombreEmpresa;
	private String contactoEmpresa;
	private String telefono;
	private String email;
	private String direccion;
	private boolean activo;
	private Date fechaRegistro;
	
}
