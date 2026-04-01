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
import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Ubicacion;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IUbicacionService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("ubicacionBean")
@javax.faces.view.ViewScoped
@Data
public class UbicacionBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UbicacionBean.class);

	private List<Ubicacion> list;

	private List<Ubicacion> filteredList;

	private List<Ubicacion> listaUbicaciones;

	private IUbicacionService iUbicacionService;
	
	

	private UploadedFile uploadedFile;
	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	private Ubicacion ubicacion;

	@Inject
	public UbicacionBean(IAuditoriaService auditoriaService, IUbicacionService iUbicacionService) {
		this.auditoriaService = auditoriaService;
		this.iUbicacionService = iUbicacionService;
	}

	@PostConstruct
	public void init() {
		cargarLista();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		if (this.ubicacion == null) {
			this.ubicacion = new Ubicacion();
		}
	}

	public void cargarLista() {
		this.list = iUbicacionService.getAll();
	}
	
	public void guardarUbicacionTabla() {
		 iUbicacionService.create(listaUbicaciones);
		 this.listaUbicaciones = new ArrayList<>();
	}

	public void guardarTabla() {
		listaUbicaciones = new ArrayList<>();
		try {
			listaUbicaciones.add(ubicacion);
			this.ubicacion = new Ubicacion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irANuevaUbicacion() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a Nueva Ubicacion", idUsuario,
				nombreUsuario);
		return "/pages/admin/ubicacion/ubicaciones.xhtml?faces-redirect=true";
	}

	public String irATablaUbicaciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a tabla Ubicaciones", idUsuario,
				nombreUsuario);
		return "/pages/admin/ubicacion/tablaUbicaciones.xhtml?faces-redirect=true";
	}

	public String irADashBoard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a Dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public void actualizarUbicacion() {
		try {
			iUbicacionService.update(ubicacion);
			cargarLista();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			iUbicacionService.delete(ubicacion.getIdUbicacion());
			this.cargarLista();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cargaArchivos() {
	    BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
	    try {
	        // Validación mejorada
	        if (uploadedFile == null || uploadedFile.getFileName().isEmpty()) {
	            mensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo válido");
	            return;
	        }

	        // Llamada al servicio
	        this.listaUbicaciones = iUbicacionService.cargarArchivo(uploadedFile);
	        
	        if (this.listaUbicaciones != null && !this.listaUbicaciones.isEmpty()) {
	            mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Se han precargado " + listaUbicaciones.size() + " registros.");
	            
	            baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
	                    "Usuario " + nombreUsuario + " realizó una carga masiva",
	                    Mensajes.INFO.toString(), idUsuario);
	        } else {
	            mensaje(FacesMessage.SEVERITY_WARN, "Aviso", "El archivo está vacío o no contiene datos válidos.");
	        }

	    } catch (ExceptionMessage e) {
	        logger.error("Error de negocio: {}", e.getMessage());
	        mensaje(FacesMessage.SEVERITY_ERROR, "Error de Validación:", e.getMessage());
	        baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, "Error en carga: " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
	    } catch (Exception e) {
	        logger.error("Error inesperado: ", e);
	        mensaje(FacesMessage.SEVERITY_FATAL, "Error de Sistema:", "Ocurrió un error al procesar el archivo.");
	        baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, "Excepción: " + e.getClass().getName(), Mensajes.ERROR.toString(), idUsuario);
	    }
	}
	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
