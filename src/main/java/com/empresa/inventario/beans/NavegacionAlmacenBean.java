package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.inject.Named;

import lombok.Data;

@Named("navegacionAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionAlmacenBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String irAPerfil() {
	    return "/pages/almacen/usuario/cuentaPerfil.xhtml?faces-redirect=true";
	}
}
