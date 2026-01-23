package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Data;

@Named("navBean")
@ViewScoped
@Data
public class NavegacionBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String irAProductos() {
	    return "/pages/productos/tablaProductos.xhtml?faces-redirect=true";
	}
    
    public String irACategorias() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/categorias/tablaCategorias?faces-redirect=true";
    }
    
    public String irAMovimientos() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/movimientos/tablaMovimientos?faces-redirect=true";
    }
    
    public String irAUsuarios() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/usuarios/tablaUsuarios?faces-redirect=true";
    }

    // Volver al index
    public String irAlIndex() {
        return "/index?faces-redirect=true";
    }
    
    
}