package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IMovimientosService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("movimientosBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosBean implements Serializable {


	private static final long serialVersionUID = 1L;

	private boolean modoManual = false;

	private transient List<Movimientos> list;

	private transient List<Movimientos> filteredList;

	private Movimientos movimientos;

	private transient List<Productos> listProductos;

	private Boolean mostrarScanner = false;

	private String infoProductoExtra;

	private IMovimientosService service;

	private IProductoService iProductoService;

	private List<Movimientos> listaMovimientosGuardar = new ArrayList<Movimientos>();

	private int idUsuario;

	private String nombreUsuario;

	private Usuario user;
	
	private IAuditoriaService auditoriaService;

	@Inject
	public MovimientosBean(IAuditoriaService auditoriaService, IProductoService iProductoService, IMovimientosService service) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
		this.service = service;		;
	}

	@PostConstruct
	public void init() {
		this.movimientos = new Movimientos();
		listaMovimientos();
		listProductos = iProductoService.getAll();
		user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public List<Movimientos> listaMovimientos() {
		try {
			list = service.getAll();
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void save() throws Exception {
		listaMovimientosGuardar.add(movimientos);
		int idActual = this.movimientos.getIdUsuario();
		this.movimientos = new Movimientos();
		this.movimientos.setIdUsuario(idActual);

		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Añadir registro a tabla ");
		auditoria.setAccion(Mensajes.USUARIO+ nombreUsuario + " registro un elemento a la tabla");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
	}

	public void saveTable() throws Exception {
		if (listaMovimientosGuardar != null && !listaMovimientosGuardar.isEmpty()) {
			service.save(listaMovimientosGuardar);
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Guardar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " guardo un registro");
			auditoria.setNivel(String.valueOf(Mensajes.INFO));
			auditoriaService.registroAuditoria(auditoria);
		}
		if (listaMovimientosGuardar != null) {
			listaMovimientosGuardar.clear();
		}
	}

	public String irANuevoMovimiento() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego hacia nuevo movimiento");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego hacia dashboard");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego hacia tabla movimientos");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}

	public void toggleScanner() {
		this.mostrarScanner = !this.mostrarScanner;
		this.movimientos.setCodigoBarras(null);
	}

	public void cargarInfoScanner() throws Exception {
		String codigo = this.movimientos.getCodigoBarras();
		if (codigo != null && !codigo.isEmpty()) {
			this.infoProductoExtra = "Cargado: " + codigo + " - Producto encontrado";
			Productos productos = iProductoService.getByCodigoBarras(codigo);
			movimientos.setIdProducto(productos.getIdProducto());
		} else {
			this.infoProductoExtra = "Código no válido";
		}
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
