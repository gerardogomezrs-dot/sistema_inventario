package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IProveedorService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("proveedorBean")
@ViewScoped
@Data
public class ProveedorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProveedorBean.class);

	private transient List<Proveedor> listaProveedorGuardar = new ArrayList<>();

	private transient List<Proveedor> list;

	private transient UploadedFile uploadedFile;

	private transient Proveedor proveedor;

	private Integer progreso = 0;

	private transient IProveedorService iProveedorService;

	private int idUsuario;

	private String nombreUsuario;
	
	private String filtro;

	private transient IAuditoriaService auditoriaService;

	@Inject
	ProveedorBean(IAuditoriaService auditoriaService, IProveedorService iProveedorService) {
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
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irANuevoProveedor() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nuevo producto", idUsuario,
				nombreUsuario);

		return "/pages/admin/proveedores/proveedores.xhtml?faces-redirect=true";
	}

	public String irATablaProvedor() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Tabla Proveedor", "entro a tabla Proveedor", idUsuario,
				nombreUsuario);
		return "/pages/admin/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}

	public void guardarTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			this.listaProveedorGuardar.add(this.proveedor);
			this.proveedor = new Proveedor();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardarTablaProveedor() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			iProveedorService.save(listaProveedorGuardar);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro guardado", "El registro fue guardado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);
			this.listaProveedorGuardar = new ArrayList<>();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}

		if (listaProveedorGuardar.isEmpty()) {
			this.listaProveedorGuardar.clear();
			this.proveedor = new Proveedor();
		}
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void cargarListaProveedores() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			list = iProveedorService.proveedors();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminarProveedor() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			iProveedorService.delete(proveedor.getIdProveedor());
			this.list = iProveedorService.proveedors();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro eliminado", "El Registro fue eliminado correctamente"));

			baseBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void actualizarProveedor() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			iProveedorService.update(proveedor);
			this.list = iProveedorService.proveedors();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro Actualizado", "El Registro fue actualizado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void cargaArchivos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProveedorGuardar = new ArrayList<>();
			listaProveedorGuardar = iProveedorService.uploadFiles(uploadedFile);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
	public void aplicarFiltroExterno() {
		if (filtro != null && !filtro.trim().isEmpty()) {
			this.list = iProveedorService.byNombreEmpresa(filtro);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultados", "Mostrando resultados para: " + filtro));
		} else {
			this.list = iProveedorService.proveedors();
		}
	}

}
