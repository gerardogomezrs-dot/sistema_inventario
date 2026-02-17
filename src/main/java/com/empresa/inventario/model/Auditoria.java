package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class Auditoria {

	public  Auditoria() {
	}
	
	private int idAuditoria;
	private Date fechaAuditoria;
	private String claseOrigen;
	private String metodo;
	private String accion;
	private String nivel;
}
