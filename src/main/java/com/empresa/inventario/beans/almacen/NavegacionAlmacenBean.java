package com.empresa.inventario.beans.almacen;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

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

@Named("navegacionAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class NavegacionAlmacenBean implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private int idUsuario;

	private String textoBusqueda;

	private String nombreUsuario;

	private transient IAuditoriaService auditoriaService;

	private List<Productos> listaProductos;

	private int stockBajoCount;
	private int movimientosHoyCount;

	private IProductoService iProductoService;

	private ICategoriaService iCategoriaService;

	private IProveedorService iProveedorService;

	@Inject
	public NavegacionAlmacenBean(IAuditoriaService auditoriaService, IProductoService iProductoService,
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
		actualizarDashboard();
		movimientosHoyCount = iProductoService.totalMovimientos(idUsuario);
		stockBajoCount = iProductoService.totalStockBajo();
	}

	public void actualizarDashboard() {

	}

	public String irAPerfilUsuario() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.PERFIL_USUARIO, "entro a consultar su perfil",
				idUsuario, nombreUsuario);
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
		baseBean.registrarNavegacion(auditoriaService, Mensajes.MODULO_MOVIMIENTOS, "entro al modulo de movimientos",
				idUsuario, nombreUsuario);
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

		redirigirSegunBusqueda(esProveedor, esCategoria, esProducto);
	}

	public void redirigirSegunBusqueda(boolean esProveedor, boolean esCategoria, boolean esProducto) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String queryParam = "?faces-redirect=true&query=" + textoBusqueda;
			String url = "";
			if (esProveedor) {
				url = "../almacen/proveedores/tablaProveedores.xhtml" + queryParam;
			} else if (esCategoria) {
				url = "../almacen/categorias/tablaCategorias.xhtml" + queryParam;
			} else if (esProducto) {
				url = "../almacen/productos/tablaProductos.xhtml" + queryParam;
			} else {
				url = "../almacen/dashboard.xhtml";
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No se encontro"));
			}
			context.getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
