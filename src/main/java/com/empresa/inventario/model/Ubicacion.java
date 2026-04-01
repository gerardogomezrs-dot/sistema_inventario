package com.empresa.inventario.model;

import lombok.Data;

@Data
public class Ubicacion {
	private int idUbicacion;
	private String pasillo;
	private String estante;
	private String nivel;
	private String codigoControl;
	private String estado;
}

