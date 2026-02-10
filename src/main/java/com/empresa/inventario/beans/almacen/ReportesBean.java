package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import lombok.Data;

@Named("reportesAlmacenBean") // Nombre para usar en el XHTML
@javax.faces.view.ViewScoped
@Data
public class ReportesBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReportesBean() {
	}
	@PostConstruct
	public void init() {
		
	}
}
