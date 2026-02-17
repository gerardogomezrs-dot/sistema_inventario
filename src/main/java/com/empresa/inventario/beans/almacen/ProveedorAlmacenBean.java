package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.service.IProveedorService;

import lombok.Data;

@Named("proveedorAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedorAlmacenBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<Proveedor> listaProveedorGuardar = new ArrayList<Proveedor>();

	private List<Proveedor> list;

	private UploadedFile uploadedFile;

	private Proveedor proveedor;
	
	private Integer progreso = 0;
	
	@Inject
	private IProveedorService iProveedorService;
	
	public ProveedorAlmacenBean (){
	}
	
	@PostConstruct
	public void init() {
		proveedor = new Proveedor();		
		cargarListaProveedores();
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
	
	public void cargarListaProveedores(){
		try {
		list = iProveedorService.proveedors();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarProveedor() {
		try {
		iProveedorService.update(proveedor);
		cargarListaProveedores();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro actualizado", "El Registro fue actualizado correctamente"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void eliminarProveedor() {
		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Eliminado", "El Registro fue eliminado correctamente"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}