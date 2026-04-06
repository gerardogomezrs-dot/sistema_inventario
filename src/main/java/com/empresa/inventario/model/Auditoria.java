package com.empresa.inventario.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Auditoria implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int idAuditoria;
	private Date fechaAuditoria;
	private int idUsuario;
	private String claseOrigen;
	private String metodo;
	private String accion;
	private String nivel;
}
