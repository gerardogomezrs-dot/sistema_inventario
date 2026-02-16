package com.empresa.inventario.beans.admin;

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
		return "/pages/admin/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}

	public void guardarTabla() {
		this.listaProveedorGuardar.add(this.proveedor);
		this.proveedor = new Proveedor();
	}

	public void guardarTablaProveedor() {	
		List<Proveedor> proveedors = new ArrayList<Proveedor>(listaProveedorGuardar);
		System.out.println("Tamaño de lista "+ proveedors.size());
		iProveedorService.save(proveedors);
		listaProveedorGuardar = new ArrayList<Proveedor>();
	}

	public void cargarListaProveedores() {
		list = iProveedorService.proveedors();
	}
	
	public void eliminarProveedor() {
		try {
		iProveedorService.delete(proveedor.getIdProveedor());
		this.list = iProveedorService.proveedors(); 
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Proveedor eliminado", "El Provedor fue eliminado correctamente"));
		}catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	public void actualizarProveedor() {
		try {
		iProveedorService.update(proveedor);
		this.list = iProveedorService.proveedors(); 
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Proveedor Actualizado", "El Provedor fue actualizado correctamente"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
