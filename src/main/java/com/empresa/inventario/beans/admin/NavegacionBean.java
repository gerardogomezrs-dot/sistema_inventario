package com.empresa.inventario.beans.admin;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.service.IProveedorService;
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

	private String textoBusqueda;

	private List<Productos> listaProductos;

	private IProductoService iProductoService;

	private ICategoriaService iCategoriaService;

	private IProveedorService iProveedorService;

	@Inject
	public NavegacionBean(IAuditoriaService auditoriaService, IProductoService iProductoService,
			ICategoriaService iCategoriaService, IProveedorService iProveedorService) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
		this.iCategoriaService = iCategoriaService;
		this.iProveedorService = iProveedorService;
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
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
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a tabla Ubicaciones", idUsuario,
				nombreUsuario);
		return "/pages/admin/ubicacion/tablaUbicaciones.xhtml?faces-redirect=true";
	}

	public void filtrarBusqueda() {
		System.err.println("Iniciando búsqueda: " + textoBusqueda);

		if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
			throw new ExceptionMessage("Error: El campo de búsqueda está vacío");
		}

		String filtro = textoBusqueda.toLowerCase().trim();
		boolean esProveedor = iProveedorService.proveedors().stream()
				.anyMatch(prov -> prov.getNombreEmpresa().toLowerCase().contains(filtro));
		boolean esCategoria = iCategoriaService.getAllCategorias().stream()
				.anyMatch(cat -> cat.getNombre().toLowerCase().contains(filtro));
		listaProductos = iProductoService.getAll().stream().filter(p -> p.getNombre().toLowerCase().contains(filtro))
				.collect(Collectors.toList());

		boolean esProducto = !listaProductos.isEmpty();

		redirigirSegundaBusqueda(esProveedor, esCategoria, esProducto);
	}

	public void redirigirSegundaBusqueda(boolean esProveedor, boolean esCategoria, boolean esProducto) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String queryParam = "?faces-redirect=true&query=" + textoBusqueda;
			String url = "";
			if (esProveedor) {
				url = "../admin/proveedores/tablaProveedores.xhtml" + queryParam;
			} else if (esCategoria) {
				url = "../admin/categorias/tablaCategorias.xhtml" + queryParam;
			} else if (esProducto) {
				url = "../admin/productos/tablaProductos.xhtml" + queryParam;
			} else {
				url = "../admin/dashboard.xhtml";
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No se encontro"));
			}
			context.getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}