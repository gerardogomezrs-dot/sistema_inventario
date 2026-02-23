package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;

import lombok.Data;

@Named("navegacionManagerBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idUsuario;

	private String nombreUsuario;

	@Inject
	private IAuditoriaService auditoriaService;

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public String irAPerfil() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Perfil Usuarios");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Perfil");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/usuario/cuentaPerfilAlmacen.xhtml?faces-redirect=true";
	}

	public String irAGestionCategorias() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo productos");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Gestion Categorias");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public String irAGestionProductos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Productos");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Gestion Productos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irAGestionReportes() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Reportes");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Gestion Reportes");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irAGestionProveedores() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Proveedores");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Gestion Proveedores");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/proveedores/tablaProveedores.xhtml?faces-redirect=true";
	}

	public String irAGestionMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Movimientos");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a Gestion Movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}

}
