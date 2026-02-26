package com.empresa.inventario.beans.stockmanager;

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
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("proveedorManagerBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedorManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Proveedor> listaProveedorGuardar = new ArrayList<>();

	private List<Proveedor> list;

	private transient UploadedFile uploadedFile;

	private Proveedor proveedor;

	private Integer progreso = 0;

	private IProveedorService iProveedorService;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public ProveedorManagerBean(IAuditoriaService auditoriaService, IProveedorService iProveedorService) {
		this.auditoriaService = auditoriaService;
		this.iProveedorService = iProveedorService;
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
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irANuevoProveedor() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a nuevo proveedor");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/proveedores/proveedores.xhtml?faces-redirect=true";
	}

	public String irATablaProvedor() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a tabla proveedor");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/proveedores/tablaProveedor.xhtml?faces-redirect=true";
	}

	public void cargarListaProveedores() {
		try {
			list = iProveedorService.proveedors();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void actualizarProveedor() {
		try {
			iProveedorService.update(proveedor);
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro actualizado", "El Registro fue actualizado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void eliminarProveedor() {
		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Eliminado", "El Registro fue eliminado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Eliminar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una eliminación");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void guardarTabla() {
		listaProveedorGuardar.add(proveedor);
		this.proveedor = new Proveedor();
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar Registro Tabla");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo un guardo un registro a la tabla");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void guardarTablaProveedor() {
		progreso = 0;
		if (listaProveedorGuardar != null && !listaProveedorGuardar.isEmpty()) {
			this.progreso = 0;
			List<Proveedor> copiaParaGuardar = new ArrayList<>(listaProveedorGuardar);
			if (copiaParaGuardar.isEmpty()) {
				return;
			}
			CompletableFuture.runAsync(() -> {
				try {
					iProveedorService.save(copiaParaGuardar, valor -> this.progreso = valor);
				} catch (Exception e) {
					e.getMessage();
					Auditoria auditoria = new Auditoria();
					auditoria.setFechaAuditoria(new Date());
					auditoria.setIdUsuario(idUsuario);
					auditoria.setClaseOrigen(this.getClass().getName());
					auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
					auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
					auditoria.setNivel(String.valueOf(Mensajes.ERROR));
					auditoriaService.registroAuditoria(auditoria);
				}
			});

		}
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar Registro Tabla");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo guardado de registros");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
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
			listaProveedorGuardar = new ArrayList<>();
			listaProveedorGuardar = iProveedorService.uploadFiles(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());

			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);

		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}