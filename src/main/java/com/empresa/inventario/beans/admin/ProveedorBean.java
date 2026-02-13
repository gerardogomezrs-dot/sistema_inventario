package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.service.IProveedorService;

import lombok.Data;

@Named("proveedorBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Proveedor> listaProveedorGuardar = new ArrayList<Proveedor>();

	private List<Proveedor> list;

	private UploadedFile uploadedFile;

	private Proveedor proveedor;
	
	private Integer progreso = 0;
	
	@Inject
	private IProveedorService iProveedorService;

	ProveedorBean() {	
	}

	@PostConstruct
	public void init() {
		proveedor = new Proveedor();
		cargarListaProveedores();
	}

	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}
	
	public String irANuevoProveedor() {
		return "/pages/admin/proveedores/proveedores.xhtml?faces-redirect=true";
	}
	
	public String irATablaProvedor() {
		return "/pages/admin/proveedores/tablaProveedor.xhtml?faces-redirect=true";
	}
	
	public void guardarTabla() {
		this.listaProveedorGuardar.add(this.proveedor);
		this.proveedor = new Proveedor();
	}
	
	public void guardarTablaProveedor() {
		
	}
	
	public void cargarListaProveedores() {
		list = iProveedorService.proveedors();
	}

}
