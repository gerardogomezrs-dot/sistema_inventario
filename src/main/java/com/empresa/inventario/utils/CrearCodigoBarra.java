package com.empresa.inventario.utils;

public class CrearCodigoBarra {

	public static String generarCodigoBarra() {
		return "PRD-" + System.currentTimeMillis();
		
	}
}
