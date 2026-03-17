package com.empresa.inventario.beans.almacen;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.utils.Mensajes;

@Named("navegacionAlmacenBean")
@javax.faces.view.ViewScoped
public class NavegacionAlmacenBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	


	private int idUsuario;

	private String nombreUsuario;

	private transient IAuditoriaService auditoriaService;

	@Inject
	public NavegacionAlmacenBean(IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public String irAPerfilUsuario() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.PERFIL_USUARIO, "entro a consultar su perfil", idUsuario, nombreUsuario);
		return "/pages/almacen/usuarios/perfilUsuario.xhtml?faces-redirect=true";
	}

	public String irAGestionProductos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PRODUCTOS, "entro al modulo de productos",
				idUsuario, nombreUsuario);
		return "/pages/almacen/productos/tablaProductos.xhtml?faces-redirect=true";
	}
	
	public String irAGestionProveedores() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PRODUCTOS, "entro al modulo de proveedores",
				idUsuario, nombreUsuario);
		return "/pages/almacen/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}

	public String irAGestionMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_MOVIMIENTOS, "entro al modulo de movimientos", idUsuario, nombreUsuario);
		return "/pages/almacen/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}
	
	public String irAGestionCategorias() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PRODUCTOS, "entro al modulo de categorias",
				idUsuario, nombreUsuario);
		return "/pages/almacen/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}
	
	public String irAGestionReportes() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/almacen/reportes/reportesDashboard.xhtml?faces-redirect=true";
	}

}
