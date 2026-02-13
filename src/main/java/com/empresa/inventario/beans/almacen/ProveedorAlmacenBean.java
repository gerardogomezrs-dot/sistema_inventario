package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Proveedor;

import lombok.Data;

@Named("proveedorAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedorAlmacenBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private List<Proveedor> listaProveedorGuardar = new ArrayList<Proveedor>();

	private List<Proveedor> list;

	private UploadedFile uploadedFile;

	private Proveedor proveedor;
	
	private Integer progreso = 0;
	
	public ProveedorAlmacenBean (){
		
	}
	
	@PostConstruct
	public void init() {
		
	}

	public String irADashboard() {
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}
	
	public String irANuevoProveedor() {
		return "/pages/almacen/proveedores/proveedores.xhtml?faces-redirect=true";
	}
	
	public String irATablaProveedor() {
		return "/pages/almacen/proveedores/tablaProveedor.xhtml?faces-redirect=true";
	}
}
