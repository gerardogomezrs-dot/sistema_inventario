package com.empresa.inventario.model;

import java.util.Date;

import lombok.Data;

@Data
public class Auditoria {
	
	private int idAuditoria;
	private Date fechaAuditoria;
	private int idUsuario;
	private String claseOrigen;
	private String metodo;
	private String accion;
	private String nivel;
}
