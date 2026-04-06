package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 

import java.util.ArrayList;
import java.util.Base64;
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

@Named("movimientosBean")
@ViewScoped
@Data
public class MovimientosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovimientosBean.class);

	private transient UploadedFile uploadedFile;

	private transient String textoBusqueda;

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

	private String codigoFiltro;

	private String url;

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
			logger.debug(e.getMessage());
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
		return list;
	}

	public void save() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			this.movimientos.setIdUsuario(idUsuario);
			listaMovimientosGuardar.add(movimientos);
			this.movimientos = new Movimientos();
			this.infoProductoExtra = "";
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro guardado"));
		} catch (Exception e) {
			logger.debug(e.getMessage());
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
				this.listaMovimientosGuardar.clear();
				this.movimientos = new Movimientos();
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro guardado"));
		} catch (Exception e) {
			logger.debug(e.getMessage());
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

	public void cargarInfoScanner(String codigoFiltro) throws Exception {
		String codigo = this.movimientos.getCodigoBarras();
		Productos productos = new Productos();
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			productos = iProductoService.getByCodigoBarras(codigoFiltro);
			if (productos.getCodigoBarras().equals(codigo)) {
				this.infoProductoExtra = "Cargado: " + codigo + " - Producto encontrado";
				this.movimientos.setIdProducto(productos.getIdProducto());
				this.movimientos.setNombreProducto(productos.getNombre());
				this.movimientos.setProductoExistencias(productos.getStockActual());
				this.movimientos.setImagenProducto(productos.getArchivo());
				this.movimientos.setUbicacion(
						productos.getUbicacion().getPasillo() + "/" + productos.getUbicacion().getEstante());
				this.infoProductoExtra = "";
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto encontrado."));
			} else {
				url = "../dashboard.xhtml";
				context.getExternalContext().redirect(url);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No se encontro"));
			}
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		}
	}

	public void cargarInfoScanner2() throws Exception {
		String codigoBarras = this.movimientos.getCodigoBarras();
		Productos productos = new Productos();

		try {
			productos = iProductoService.getByCodigoBarras(codigoBarras);
			if (productos.getCodigoBarras().equals(codigoBarras)) {
				this.infoProductoExtra = "Cargado: " + codigoBarras + " - Producto encontrado";
				this.movimientos.setIdProducto(productos.getIdProducto());
				this.movimientos.setNombreProducto(productos.getNombre());
				this.movimientos.setProductoExistencias(productos.getStockActual());
				this.movimientos.setImagenProducto(productos.getArchivo());
				this.movimientos.setUbicacion(
						productos.getUbicacion().getPasillo() + "/" + productos.getUbicacion().getEstante());
				this.infoProductoExtra = "";
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto encontrado."));
			}else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "No se encontro"));
				this.movimientos = new Movimientos();
			}
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void procesarEscaneo() {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = "../admin/movimientos/escaneoRapido.xhtml?faces-redirect=true&query=" + codigoFiltro;
		try {
			context.getExternalContext().redirect(url);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void procesarEscaneoMovimientos() {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = "../movimientos/movimientos.xhtml?faces-redirect=true&query=" + codigoFiltro;
		try {
			context.getExternalContext().redirect(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void aplicarFiltroExterno() throws Exception {
		this.movimientos.setCodigoBarras(codigoFiltro);
		cargarInfoScanner(codigoFiltro);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultados",
				"Mostrando resultados para: " + codigoFiltro));
	}

	public String convertirABase64(byte[] bytes) {
		if (bytes != null && bytes.length > 0) {
			return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
		}
		return "";
	}

	public void limpiarCampos() {
		this.movimientos = new Movimientos();
		this.infoProductoExtra = "";
	}

	public void limpiarParaNuevoEscaneo() {
		this.movimientos.setCodigoBarras("");
	}
}
