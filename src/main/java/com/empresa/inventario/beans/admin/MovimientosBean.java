package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
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

	private transient UploadedFile uploadedFile;

	private boolean modoManual = false;

	private transient List<Movimientos> list;

	private transient List<Movimientos> filteredList;

	private transient Movimientos movimientos;

	private transient List<Productos> listProductos;

	private Boolean mostrarScanner = false;

	private String infoProductoExtra;

	private IMovimientosService service;

	private IProductoService iProductoService;

	private transient List<Movimientos> listaMovimientosGuardar = new ArrayList<>();

	private int idUsuario;

	private String nombreUsuario;

	private transient Usuario user;

	private transient IAuditoriaService auditoriaService;

	@Inject
	public MovimientosBean(IAuditoriaService auditoriaService, IProductoService iProductoService,
			IMovimientosService service) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
		this.service = service;
	}

	@PostConstruct
	public void init() {
		this.movimientos = new Movimientos();
		listaMovimientos();
		listProductos = iProductoService.getAll();
		user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		movimientos.setIdUsuario(idUsuario);
	}

	public List<Movimientos> listaMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			list = service.getAll();
		} catch (ExceptionMessage e) {
			e.getMessage();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
		return list;
	}

	public void save() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			listaMovimientosGuardar.add(movimientos);
			int idActual = this.movimientos.getIdUsuario();
			this.movimientos = new Movimientos();
			this.movimientos.setIdUsuario(idActual);

			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void saveTable() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (listaMovimientosGuardar != null && !listaMovimientosGuardar.isEmpty()) {
				service.save(listaMovimientosGuardar);
				baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
						Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro",
						Mensajes.INFO.toString(), idUsuario);
			}
			if (listaMovimientosGuardar != null) {
				listaMovimientosGuardar.clear();
			}
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public String irANuevoMovimiento() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Nuevo Movimiento", "entro a nuevo movimiento", idUsuario,
				nombreUsuario);
		return "/pages/admin/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashbard", "entro a dashboard", idUsuario, nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Tabla Movimientos", "entro a tabla movimientos", idUsuario,
				nombreUsuario);
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

	public void cargarArchivo() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		List<Movimientos> liMovimientos = new ArrayList<>();
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaMovimientosGuardar = new ArrayList<>();
			liMovimientos = service.cargaMasiva(uploadedFile);
			this.listaMovimientosGuardar = new ArrayList<>(liMovimientos);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

			baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);

		} catch (ExceptionMessage e) {
			e.getMessage();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
