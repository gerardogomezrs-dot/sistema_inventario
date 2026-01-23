package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Usuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idUsuario;
	private String nombre;
	private String userName;
	private String password;
	private String rol;
	private String permisos;
	private boolean activo;
	
}
