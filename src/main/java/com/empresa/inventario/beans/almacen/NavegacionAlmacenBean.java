package com.empresa.inventario.beans.almacen;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IProductoService;
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

	@Inject
	public NavegacionAlmacenBean(IAuditoriaService auditoriaService, IProductoService iProductoService) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
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

		if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
			// this.listaProductos;
			return;
		}
		listaProductos = iProductoService.getAll();
		String filtro = textoBusqueda.toLowerCase();

		listaProductos.stream().filter(p -> (p.getNombre() != null && p.getNombre().toLowerCase().contains(filtro))
				|| (p.getProveedor() != null && p.getProveedor().getNombreEmpresa().toLowerCase().contains(filtro))
				|| (p.getCategorias() != null && p.getCategorias().getNombre().toLowerCase().contains(filtro)))
				.collect(Collectors.toList());

		redirigirSegunBusqueda(filtro);
	}

	public void redirigirSegunBusqueda(String filtro) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();

			boolean esProducto = listaProductos.stream().anyMatch(p ->  p.getNombre().toLowerCase().contains(filtro));
			
			boolean esProveedor = listaProductos.stream().anyMatch(p -> p.getProveedor() != null
					&& p.getProveedor().getNombreEmpresa().toLowerCase().contains(filtro));

			boolean esCategoria = listaProductos.stream().anyMatch(
					p -> p.getCategorias() != null && p.getCategorias().getNombre().toLowerCase().contains(filtro));

			if (esProveedor) {
				String url = "../almacen/proveedores/tablaProveedores.xhtml?faces-redirect=true&query=" + textoBusqueda;
				context.getExternalContext().redirect(url);
			} 
			else if (esCategoria) {
				String url = "../almacen/categorias/tablaCategorias.xhtml?faces-redirect=true&query=" + textoBusqueda;
				context.getExternalContext().redirect(url);
			} 
			else if(esProducto){
				String url = "../almacen/productos/tablaProductos.xhtml?faces-redirect=true&query=" + textoBusqueda;
				context.getExternalContext().redirect(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
