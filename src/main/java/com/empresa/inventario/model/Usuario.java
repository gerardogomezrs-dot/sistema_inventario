package com.empresa.inventario.model;

import lombok.Data;

@Data
public class Usuario {
	
	private int idUsuario;
	private String nombre;
	private String userName;
	private String password;
	private String rol;
	private String permisos;
	private boolean activo;
	
}
