package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
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
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		auditoriaBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irANuevoProveedor() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		auditoriaBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nuevo proveedor",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/proveedores/proveedores.xhtml?faces-redirect=true";
	}

	public String irATablaProvedor() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		auditoriaBean.registrarNavegacion(auditoriaService, "Tabla Proveedor", "entro a tabla Proveedor", idUsuario,
				nombreUsuario);
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
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		try {
			iProveedorService.update(proveedor);
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro actualizado", "El Registro fue actualizado correctamente"));
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);

		} catch (Exception e) {
			e.getMessage();
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminarProveedor() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			cargarListaProveedores();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Eliminado", "El Registro fue eliminado correctamente"));

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(), idUsuario);
		} catch (Exception e) {
			e.getMessage();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardarTabla() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		listaProveedorGuardar.add(proveedor);
		this.proveedor = new Proveedor();

		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
				Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
				idUsuario);
	}

	public void guardarTablaProveedor() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		if (listaProveedorGuardar != null && !listaProveedorGuardar.isEmpty()) {

			try {
				iProveedorService.save(listaProveedorGuardar);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro guardado", "El registro fue guardado correctamente"));
				auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
						Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro",
						Mensajes.INFO.toString(), idUsuario);
			} catch (Exception e) {
				e.getMessage();
				auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR,
						Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
			}

			listaProveedorGuardar.clear();
			this.proveedor = new Proveedor();
		}

	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void cargaArchivos() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProveedorGuardar = new ArrayList<>();
			listaProveedorGuardar = iProveedorService.uploadFiles(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);

		} catch (Exception e) {
			e.getMessage();

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}