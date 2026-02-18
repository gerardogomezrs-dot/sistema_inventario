package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.admin.MovimientosBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IMovimientosService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("movimientosAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosAlmacenBean implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(MovimientosBean.class);

	private static final long serialVersionUID = 1L;

	private boolean modoManual = false;

	private List<Movimientos> list;

	private List<Movimientos> filteredList;

	private Movimientos movimientos;

	private List<Productos> listProductos;

	private Boolean mostrarScanner = false;

	private String infoProductoExtra;

	@Inject
	private IMovimientosService service;

	@Inject
	private IProductoService iProductoService;

	private List<Movimientos> listaMovimientosGuardar = new ArrayList<Movimientos>();

	private int idUsuario;

	private String nombreUsuario;

	private Usuario user;

	@Inject
	private IAuditoriaService auditoriaService;

	public MovimientosAlmacenBean() {

	}

	@PostConstruct
	public void init() {
		try {
			this.movimientos = new Movimientos();
			listaMovimientos();
			listProductos = iProductoService.getAll();
			user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");

			if (user != null) {
				this.movimientos.setIdUsuario(user.getIdUsuario());
				logger.info("LOG: Usuario recuperado de sesión: " + user.getNombre());

			} else {
				logger.info("LOG: No hay ninguna sesión activa con 'sessionUsuario'");
			}

			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();

		} catch (Exception e) {
			logger.error("Error en init de MovimientosBean: " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public List<Movimientos> listaMovimientos() throws Exception {
		try {
			list = service.getAll();
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		} catch (Exception e) {

		}
		return list;
	}
	
	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
	
	public String irANuevoMovimiento() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Nuevo movimiento");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego hacia nuevo movimiento");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public void save() {
		listaMovimientosGuardar.add(movimientos);
		this.movimientos = new Movimientos();
	}
	
	
	public String irADashboard() {
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		return "/pages/almacen/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
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
	

	public void saveTable() throws Exception {
		if (listaMovimientosGuardar != null && !listaMovimientosGuardar.isEmpty()) {
			service.save(listaMovimientosGuardar);

			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Guardar");
			auditoria.setAccion("El usuario " + nombreUsuario + " guardo un registro");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		}
		if (listaMovimientosGuardar != null) {
			listaMovimientosGuardar.clear();
		}
	}

}
