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
	
	/**
     * Método centralizado para registrar auditoría
     */
    private void registrarNavegacion(Object modulo, String accion) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFechaAuditoria(new Date());
        auditoria.setIdUsuario(idUsuario);
        auditoria.setClaseOrigen(this.getClass().getName());
        auditoria.setMetodo(modulo.toString());
        auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " " + accion);
        auditoria.setNivel(Mensajes.INFO.toString());
        auditoriaService.registroAuditoria(auditoria);
    }

	public String irAProductos() {
		registrarNavegacion(Mensajes.MODULO_PRODUCTOS, "entro al modulo de productos");
		return "/pages/admin/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irACategorias() {
		registrarNavegacion(Mensajes.MODULO_CATEGORIAS, "entro al modulo de categorias");
		return "/pages/admin/categorias/tablaCategorias?faces-redirect=true";
	}

	public String irAMovimientos() {
		registrarNavegacion(Mensajes.MODULO_MOVIMIENTOS, "entro al modulo de movimientos");
		return "/pages/admin/movimientos/tablaMovimientos?faces-redirect=true";
	}

	public String irAUsuarios() {
		registrarNavegacion(Mensajes.MODULO_USUARIOS, "entro al modulo de Usuarios");
		return "/pages/admin/usuarios/tablaUsuarios?faces-redirect=true";
	}

	public String irAReportes() {
		registrarNavegacion(Mensajes.MODULO_REPORTES, "entro al modulo de Reportes");
		return "/pages/admin/reportes/reportes?faces-redirect=true";
	}

	public String irAPerfilUsuario() {
		registrarNavegacion(Mensajes.PERFIL_USUARIO, "entro a consultar su perfil");
		return "/pages/admin/usuarios/usuarioPerfil?faces-redirect=true";
	}

	public String irAProveedores() {
		registrarNavegacion(Mensajes.MODULO_PROVEEDORES, "entro a gestión proveedores");
		return "/pages/admin/proveedores/tablaProveedores?faces-redirect=true";
	}

	public String irAAuditoria() {
		registrarNavegacion(Mensajes.GESTION_AUDITORIAS, "entro a gestión auditoria");
		return "/pages/admin/auditoria/tablaAuditoria?faces-redirect=true";
	}

	public String irADashboard() {
		registrarNavegacion(Mensajes.NAVEGACION, "navego a dashboard");
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

}