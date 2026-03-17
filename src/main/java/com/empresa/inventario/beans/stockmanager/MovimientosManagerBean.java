package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.slf4j.LoggerFactory;

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

@Named("movimientosManagerBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovimientosManagerBean.class);

	private transient UploadedFile uploadedFile;

	private boolean modoManual = false;

	private transient List<Movimientos> list;

	private transient List<Movimientos> filteredList;

	private transient Movimientos movimientos;

	private transient List<Productos> listProductos;

	private Boolean mostrarScanner = false;

	private String infoProductoExtra;

	private transient List<Movimientos> listaMovimientosGuardar = new ArrayList<>();

	private int idUsuario;

	private String nombreUsuario;

	private transient Usuario user;

	private transient IAuditoriaService auditoriaService;

	private transient IMovimientosService service;

	private transient IProductoService iProductoService;

	@Inject
	public MovimientosManagerBean(IAuditoriaService auditoriaService, IMovimientosService service,
			IProductoService iProductoService) {
		this.auditoriaService = auditoriaService;
		this.service = service;
		this.iProductoService = iProductoService;
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
			logger.debug(e.getMessage());
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
		return list;
	}

	public String irANuevoMovimiento() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nuevo Movimiento", idUsuario,
				nombreUsuario);
		return "/pages/stock_manager/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Tabla Movimientoa", "entro a tabla Movimientoa", idUsuario,
				nombreUsuario);

		return "/pages/stock_manager/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
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
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
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
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
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
			liMovimientos = service.cargaMasiva(uploadedFile, idUsuario);
			this.listaMovimientosGuardar = new ArrayList<>(liMovimientos);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

			baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);

		} catch (ExceptionMessage e) {
			logger.debug(e.getMessage());
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
