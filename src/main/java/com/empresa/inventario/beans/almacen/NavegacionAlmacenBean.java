package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.inject.Named;

import lombok.Data;

@Named("navegacionAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionAlmacenBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String irAPerfil() {
	    return "/pages/almacen/usuario/cuentaPerfilAlmacen.xhtml?faces-redirect=true";
	}
	public String irAGestionCategorias() {
	    return "/pages/almacen/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}
	
	public String irAGestionProductos() {
	    return "/pages/almacen/productos/tablaProductos.xhtml?faces-redirect=true";
	}
	
	public String irAGestionReportes() {
	    return "/pages/almacen/reportes/reportes.xhtml?faces-redirect=true";
	}
	
	public String irAGestionProveedores() {
	    return "/pages/almacen/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}
}
