package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
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
	
	private MermasDevoluciones mermasDevoluciones;
	
	private IAuditoriaService auditoriaService;
	
	private int idUsuario;

	private String nombreUsuario;
	
	private List<Productos> listaProductos;

	@Inject
	public MermaDevolucionesBean(IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	public MermaDevolucionesBean() {

	}

	@PostConstruct
	public void init() {
		
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}
	
	public String irARegistrarMermaDevolucion() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/mermasDevoluciones/mermasDevoluciones.xhtml?faces-redirect=true";


}

}