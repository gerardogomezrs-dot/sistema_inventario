package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.inject.Named;

import lombok.Data;

@Named("navBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public String irAProductos() {
	    return "/pages/admin/productos/tablaProductos.xhtml?faces-redirect=true";
	}
    
    public String irACategorias() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/admin/categorias/tablaCategorias?faces-redirect=true";
    }
    
    public String irAMovimientos() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/admin/movimientos/tablaMovimientos?faces-redirect=true";
    }
    
    public String irAUsuarios() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/admin/usuarios/tablaUsuarios?faces-redirect=true";
    }

    // Volver al index
    public String irAlIndex() {
        return "/index?faces-redirect=true";
    }
    
    
}