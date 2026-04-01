package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
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

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovimientosAlmacenBean.class);

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

	private IAuditoriaService auditoriaService;

	private IMovimientosService service;

	private IProductoService iProductoService;

	private IUsuariosService iUsuariosService;

	private String codigoFiltro;

	private String codigoBarras;
	
	@Inject
	MovimientosAlmacenBean(IAuditoriaService auditoriaService, IMovimientosService service,
			IProductoService iProductoService, IUsuariosService iUsuariosService) {
		this.auditoriaService = auditoriaService;
		this.service = service;
		this.iProductoService = iProductoService;
		this.iUsuariosService = iUsuariosService;

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

	public void cargarInfoScanner(String codigoFiltro) throws Exception {
		System.err.println("Hola");
		String codigo = this.movimientos.getCodigoBarras();
		Productos productos = new Productos();

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
			} else {

			}
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", "No existe el producto");
		}
	}

	public List<Movimientos> listaMovimientos(int idUsuario) {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			list = new ArrayList<>();
			Usuario usuario = iUsuariosService.getByIdUsuario(idUsuario);
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			}
			list = service.getbyIdUsuarioMovimientos(usuario.getIdUsuario());
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

	public void saveTable() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			service.save(listaMovimientosGuardar);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);

			if (listaMovimientosGuardar != null) {
				listaMovimientosGuardar.clear();
				this.movimientos = new Movimientos();
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registros guardados"));
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void save() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			movimientos.setIdUsuario(idUsuario);
			listaMovimientosGuardar.add(movimientos);
			this.movimientos = new Movimientos();
			this.infoProductoExtra = "";
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro guardado"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
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

	public void procesarEscaneo() {
		FacesContext context = FacesContext.getCurrentInstance();
		String url = "../almacen/movimientos/escaneoRapido.xhtml?faces-redirect=true&query=" + codigoFiltro;
		try {
			context.getExternalContext().redirect(url);
		} catch (Exception e) {
			e.printStackTrace();
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Resultados", "Mostrando resultados para: " + codigoFiltro));
	}
	
	public void limpiarParaNuevoEscaneo() {
	    this.movimientos.setCodigoBarras("");
	}
}