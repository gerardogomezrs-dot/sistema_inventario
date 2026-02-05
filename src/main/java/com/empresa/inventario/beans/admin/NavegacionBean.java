package com.empresa.inventario.beans.admin;

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
    
    public String irAReportes() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/admin/reportes/reportes?faces-redirect=true";
    }
    
    public String irAPerfilUsuario() {
        // El "/" inicial indica que partimos desde webapp
        return "/pages/admin/usuarios/usuarioPerfil?faces-redirect=true";
    }
    
    public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}
    
}