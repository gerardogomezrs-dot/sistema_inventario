package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Proveedor implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int idProveedor;
	private String nombreEmpresa;
	private String contactoEmpresa;
	private String telefono;
	private String email;
	private String direccion;
	private boolean activo;
	private Date fechaRegistro;
	
}
