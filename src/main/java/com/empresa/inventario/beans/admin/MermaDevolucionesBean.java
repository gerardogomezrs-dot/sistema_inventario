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

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IMermasDevolucionesService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("mermasDevolucionesBean")
@javax.faces.view.ViewScoped
@Data
public class MermaDevolucionesBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	

	private transient MermasDevoluciones mermasDevoluciones;

	private int idUsuario;

	private String nombreUsuario;
	
	private transient Usuario usuario;

	String tipo;

	private transient List<Productos> listaProductos;

	private transient List<MermasDevoluciones> listaMermasDevoluciones;

	private transient List<MermasDevoluciones> devoluciones;

	private transient IAuditoriaService auditoriaService;

	private transient IProductoService iProductoService;

	private transient UploadedFile uploadedFile;

	private transient  IMermasDevolucionesService devolucionesService;

	private boolean tieneDevolucion;

	@Inject
	public MermaDevolucionesBean(IAuditoriaService auditoriaService, IProductoService iProductoService,
			IMermasDevolucionesService devolucionesService) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
		this.devolucionesService = devolucionesService;
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		usuario = user;
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		listaProductos();
		listaMermasDevoluciones();
		mermasDevoluciones = new MermasDevoluciones();
		this.tieneDevolucion = true;
	}

	public String irARegistrarMermaDevolucion() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a registro merma devolucion",
				idUsuario, nombreUsuario);
		return "/pages/admin/mermasDevoluciones/mermasDevoluciones.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMermasDevoluciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/mermasDevoluciones/tablaMermasDevoluciones.xhtml?faces-redirect=true";
	}

	public void listaProductos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			listaProductos = new ArrayList<>();
			listaProductos = iProductoService.getAll();
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void listaMermasDevoluciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			listaMermasDevoluciones = devolucionesService.getListaMermasDevoluciones();
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardarRegistroTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			devoluciones = new ArrayList<>();
			devoluciones.add(mermasDevoluciones);
			mermasDevoluciones = new MermasDevoluciones();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro guardado", "El registro fue guardado en la tabla"));
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}
	public void guardarRegistros() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (devoluciones.isEmpty()) {
				throw new ExceptionMessage("Lista Vacia");
			}
			devolucionesService.guardarMermasDevoluciones(devoluciones, usuario);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro guardado", "El registro fue guardado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);
			devoluciones.clear();
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}
	
	public void cargarArchivos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			devoluciones = devolucionesService.cargarArchivo(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

			baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);

		} catch (ExceptionMessage e) {
			e.printStackTrace();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}
	
	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}