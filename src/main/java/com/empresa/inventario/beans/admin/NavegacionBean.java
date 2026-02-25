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
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("navBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;
	
	@Inject
	public NavegacionBean(IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

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
		auditoria.setMetodo(Mensajes.MODULO_PRODUCTOS.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro al modulo de productos");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irACategorias() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.MODULO_CATEGORIAS.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro al modulo de categorias");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/categorias/tablaCategorias?faces-redirect=true";
	}

	public String irAMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.MODULO_MOVIMIENTOS.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro al modulo de movimientos");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/movimientos/tablaMovimientos?faces-redirect=true";
	}

	public String irAUsuarios() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.MODULO_USUARIOS.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro al modulo de Usuarios");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/tablaUsuarios?faces-redirect=true";
	}

	public String irAReportes() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.MODULO_REPORTES.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro al modulo de Reportes");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/reportes/reportes?faces-redirect=true";
	}

	public String irAPerfilUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.PERFIL_USUARIO.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro a consultar su perfil");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/usuarioPerfil?faces-redirect=true";
	}

	public String irAProveedores() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.MODULO_PROVEEDORES.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro a gestión proveedores");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/proveedores/tablaProveedores?faces-redirect=true";
	}

	public String irAAuditoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.GESTION_AUDITORIAS.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " entro a gestión auditoria");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/auditoria/tablaAuditoria?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.NAVEGACION.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a dashboard");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

}