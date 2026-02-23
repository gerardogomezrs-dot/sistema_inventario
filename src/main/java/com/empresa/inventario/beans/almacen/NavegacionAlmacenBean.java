package com.empresa.inventario.beans.almacen;

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

@Named("navegacionAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionAlmacenBean implements Serializable {/**
	 * 
	 */
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
	
	public String irAPerfilUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Perfil Usuario");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego Perfil Usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/usuarios/perfilUsuario.xhtml?faces-redirect=true";
	}
	
	public String irAGestionProductos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a Gestion Productos  ");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}
	
	public String irAGestionMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego Gestión Movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}
	
	
	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a Dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}
	
}
