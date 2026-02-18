package com.empresa.inventario.beans.admin;

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

@Named("navBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionBean implements Serializable {

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

	public String irAProductos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo productos");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro al modulo de productos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irACategorias() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo categorias");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro al modulo de categorias");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/categorias/tablaCategorias?faces-redirect=true";
	}

	public String irAMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo movimientos");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro al modulo de movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/movimientos/tablaMovimientos?faces-redirect=true";
	}

	public String irAUsuarios() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Usuarios");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro al modulo de Usuarios");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/tablaUsuarios?faces-redirect=true";
	}

	public String irAReportes() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Modulo Reportes");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro al modulo de Reportes");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/reportes/reportes?faces-redirect=true";
	}

	public String irAPerfilUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Perfil");
		auditoria.setAccion("El usuario " + nombreUsuario + " entro a consultar su perfil");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/usuarioPerfil?faces-redirect=true";
	}

	public String irAProveedores() {
		return "/pages/admin/proveedores/tablaProveedores?faces-redirect=true";
	}

	public String irAAuditoria() {
		return "/pages/admin/auditoria/tablaAuditoria?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

}