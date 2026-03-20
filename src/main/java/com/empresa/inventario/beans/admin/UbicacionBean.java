package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseAuditoriaBean;
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
}
