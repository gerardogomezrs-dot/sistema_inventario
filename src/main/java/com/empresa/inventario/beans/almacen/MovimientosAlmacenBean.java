package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IMovimientosService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.service.IUsuariosService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("movimientosAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosAlmacenBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private boolean modoManual = false;

	private List<Movimientos> list;

	private List<Movimientos> filteredList;

	private Movimientos movimientos;

	private List<Productos> listProductos;

	private Boolean mostrarScanner = false;

	private String infoProductoExtra;

	private List<Movimientos> listaMovimientosGuardar = new ArrayList<>();

	private int idUsuario;

	private String nombreUsuario;

	private Usuario user;

	private IAuditoriaService auditoriaService;

	private IMovimientosService service;

	private IProductoService iProductoService;
	
	private IUsuariosService iUsuariosService;

	@Inject
	MovimientosAlmacenBean(IAuditoriaService auditoriaService, IMovimientosService service,
			IProductoService iProductoService, IUsuariosService iUsuariosService) {
		this.auditoriaService = auditoriaService;
		this.service = service;
		this.iProductoService = iProductoService;
		this.iUsuariosService = iUsuariosService;
		;
	}

	@PostConstruct
	public void init() {

		this.movimientos = new Movimientos();
		
		listProductos = iProductoService.getAll();
		user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario");

		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		movimientos.setIdUsuario(idUsuario);
		listaMovimientos(idUsuario);
		

	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashbard", "entro a dashboard", idUsuario, nombreUsuario);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Tabla Movimientos", "entro a tabla movimientos", idUsuario,
				nombreUsuario);
		return "/pages/almacen/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}

	public String irANuevoMovimiento() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Nuevo Movimiento", "entro a nuevo movimiento", idUsuario,
				nombreUsuario);
		return "/pages/almacen/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public void toggleScanner() {
		this.mostrarScanner = !this.mostrarScanner;
		this.movimientos.setCodigoBarras(null);
	}

	public void cargarInfoScanner() {
		String codigo = this.movimientos.getCodigoBarras();
		if (codigo != null && !codigo.isEmpty()) {
			this.infoProductoExtra = "Cargado: " + codigo + " - Producto encontrado";
			Productos productos = iProductoService.getByCodigoBarras(codigo);
			movimientos.setIdProducto(productos.getIdProducto());
		} else {
			this.infoProductoExtra = "Código no válido";
		}
	}

	public List<Movimientos> listaMovimientos(int idUsuario) {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			list = new ArrayList<>();
			Usuario usuario = iUsuariosService.getByIdUsuario(idUsuario);
			if(usuario ==null) {
				System.err.println("Vacio");
			}
			System.err.println("iD " + usuario.getIdUsuario());
			list = service.getbyIdUsuarioMovimientos(usuario.getIdUsuario());
			
		} catch (ExceptionMessage e) {
			e.printStackTrace();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
		return list;
	}

	public void saveTable() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			service.save(listaMovimientosGuardar);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);

			if (listaMovimientosGuardar != null) {
				listaMovimientosGuardar.clear();
			}
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void save() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			listaMovimientosGuardar.add(movimientos);
			this.movimientos = new Movimientos();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
