package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IProveedorService;

import lombok.Data;

@Named("proveedorAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedorAlmacenBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Proveedor> listaProveedorGuardar = new ArrayList<Proveedor>();

	private List<Proveedor> list;

	private UploadedFile uploadedFile;

	private Proveedor proveedor;

	private Integer progreso = 0;

	@Inject
	private IProveedorService iProveedorService;

	private int idUsuario;

	private String nombreUsuario;

	@Inject
	private IAuditoriaService auditoriaService;

	public ProveedorAlmacenBean() {
	}

	@PostConstruct
	public void init() {
		proveedor = new Proveedor();
		cargarListaProveedores();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public String irADashboard() {
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public String irANuevoProveedor() {
		return "/pages/almacen/proveedores/proveedores.xhtml?faces-redirect=true";
	}

	public String irATablaProvedor() {
		return "/pages/almacen/proveedores/tablaProveedor.xhtml?faces-redirect=true";
	}

	public void cargarListaProveedores() {
		try {
			list = iProveedorService.proveedors();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actualizarProveedor() {
		try {
			iProveedorService.update(proveedor);
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro actualizado", "El Registro fue actualizado correctamente"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminarProveedor() {
		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Eliminado", "El Registro fue eliminado correctamente"));
		} catch (Exception e) {
			e.printStackTrace();
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Carga masiva de archivos");
			auditoria.setAccion("sE PRODUGO UN ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void guardarTabla() {
		listaProveedorGuardar.add(proveedor);
		this.proveedor = new Proveedor();
	}

	public void guardarTablaProveedor() {
		progreso = 0;
		if (listaProveedorGuardar != null && !listaProveedorGuardar.isEmpty()) {
			this.progreso = 0;
			List<Proveedor> proveedors = new ArrayList<Proveedor>(listaProveedorGuardar);
			if (proveedors.isEmpty()) {
				return;
			}
			CompletableFuture.runAsync(() -> {
				try {
					iProveedorService.save(proveedors, (valor) -> {
						this.progreso = valor;
					});
				} catch (Exception e) {
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