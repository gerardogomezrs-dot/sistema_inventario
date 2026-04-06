package com.empresa.inventario.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ubicacion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idUbicacion;
	private String pasillo;
	private String estante;
	private String nivel;
	private String codigoControl;
	private String estado;
}

