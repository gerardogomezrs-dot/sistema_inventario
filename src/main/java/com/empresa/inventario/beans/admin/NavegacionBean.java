package com.empresa.inventario.beans.admin;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.empresa.inventario.beans.BaseAuditoriaBean;
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
		// Importar org.primefaces.PrimeFaces;
		PrimeFaces.current().executeScript("PF('dlgSinStock').show()");
	}

	public String irAProductos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PRODUCTOS, "entro al modulo de productos",
				idUsuario, nombreUsuario);
		return "/pages/admin/productos/tablaProductos.xhtml?faces-redirect=true";
	}

	public String irACategorias() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_CATEGORIAS, "entro al modulo de categorias",
				idUsuario, nombreUsuario);
		return "/pages/admin/categorias/tablaCategorias?faces-redirect=true";
	}

	public String irAMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_MOVIMIENTOS, "entro al modulo de movimientos",
				idUsuario, nombreUsuario);
		return "/pages/admin/movimientos/tablaMovimientos?faces-redirect=true";
	}

	public String irAUsuarios() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_USUARIOS, "entro al modulo de Usuarios",
				idUsuario, nombreUsuario);
		return "/pages/admin/usuarios/tablaUsuarios?faces-redirect=true";
	}

	public String irAReportes() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_REPORTES, "entro al modulo de Reportes",
				idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reportes?faces-redirect=true";
	}

	public String irAPerfilUsuario() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.PERFIL_USUARIO, "entro a consultar su perfil",
				idUsuario, nombreUsuario);
		return "/pages/admin/usuarios/usuarioPerfil?faces-redirect=true";
	}

	public String irAProveedores() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_PROVEEDORES, "entro a gestión proveedores",
				idUsuario, nombreUsuario);
		return "/pages/admin/proveedores/tablaProveedores?faces-redirect=true";
	}

	public String irAAuditoria() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.GESTION_AUDITORIAS, "entro a gestión auditoria",
				idUsuario, nombreUsuario);
		return "/pages/admin/auditoria/tablaAuditoria?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irAMermasDevoluciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a tabla Mermas Devoluciones",
				idUsuario, nombreUsuario);
		return "/pages/admin/mermasDevoluciones/tablaMermasDevoluciones.xhtml?faces-redirect=true";
	}
	
	public String irAUbicaciones() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a tabla Ubicaciones",
				idUsuario, nombreUsuario);
		return "/pages/admin/ubicacion/tablaUbicaciones.xhtml?faces-redirect=true";
	}

}