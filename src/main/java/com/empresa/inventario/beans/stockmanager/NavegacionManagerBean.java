package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("navegacionManagerBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;
	
	@Inject
	public NavegacionManagerBean(IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public String irAPerfil() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.PERFIL_USUARIO, "entro a consultar su perfil",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/usuario/cuentaPerfilAlmacen.xhtml?faces-redirect=true";
	}

	public String irAGestionCategorias() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_CATEGORIAS, "entro al modulo de categorias",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public String irAGestionProductos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PRODUCTOS, "entro al modulo de productos",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irAGestionReportes() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_REPORTES, "entro al modulo de Reportes",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irAGestionProveedores() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PROVEEDORES, "entro a gestión proveedores",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}

	public String irAGestionMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_MOVIMIENTOS, "entro al modulo de movimientos",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}
	
	public String irAGestionMermasDevoluciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_MERMAS_DEVOLUCIONES, "entro al modulo de Mermas Devoluciones",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/mermasDevoluciones/tablaMermasDevoluciones.xhtml?faces-redirect=true";
	}

}
