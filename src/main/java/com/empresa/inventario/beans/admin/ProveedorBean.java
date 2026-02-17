package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.exceptions.ExceptionMessage;
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
		progreso = 0;
		if(listaProveedorGuardar != null && !listaProveedorGuardar.isEmpty()) {
			this.progreso = 0;
		List<Proveedor> proveedors = new ArrayList<Proveedor>(listaProveedorGuardar);
		if(proveedors.isEmpty()) {
			return;
		}
		for(Proveedor proveedor: proveedors) {
			System.err.println(proveedor.getNombreEmpresa());
		}
		CompletableFuture.runAsync(() ->{
			try {
				iProveedorService.save(proveedors, (valor) -> {
					this.progreso = valor;
				});
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		}
		listaProveedorGuardar.clear();
		this.proveedor = new Proveedor();
	}
	
	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void cargarListaProveedores() {
		list = iProveedorService.proveedors();
	}

	public void eliminarProveedor() {
		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			this.list = iProveedorService.proveedors();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro eliminado", "El Registro fue eliminado correctamente"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actualizarProveedor() {
		try {
			iProveedorService.update(proveedor);
			this.list = iProveedorService.proveedors();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Actualizado", "El Registro fue actualizado correctamente"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cargaArchivos() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProveedorGuardar = new ArrayList<Proveedor>();
			listaProveedorGuardar = iProveedorService.uploadFiles(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
