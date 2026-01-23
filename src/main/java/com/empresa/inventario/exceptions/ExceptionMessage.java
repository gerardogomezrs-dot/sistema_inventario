package com.empresa.inventario.exceptions;


public class ExceptionMessage extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExceptionMessage(String mensaje) {
		super(mensaje);
	}

}
