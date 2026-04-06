package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.model.Ubicacion;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.service.IProveedorService;
import com.empresa.inventario.service.IUbicacionService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("productosManagerBean")
@ViewScoped
@Data
public class ProductosManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ProductosManagerBean.class);

	// Listas de datos para la vista
	private List<Categorias> listaCategorias;
	private List<Productos> listaProductosGuardar = new ArrayList<>();
	private List<Productos> listaProductosList;
	private List<Proveedor> listaProveedores;
	private List<Ubicacion> listaUbicacion;

	// Manejo de archivos y modelo actual
	private transient UploadedFile uploadedFile;
	private transient UploadedFile uploadedImagen;
	private Productos producto;

	private Integer progreso = 0;
	private int idUsuario;
	private String nombreUsuario;
	private String filtro;

	// Inyección de Servicios (Sin transient para evitar NullPointerException en
	// ViewScoped)
	@Inject
	private IAuditoriaService auditoriaService;

	@Inject
	private IProductoService iProductoService;

	@Inject
	private ICategoriaService iCategoriaService;

	@Inject
	private IProveedorService iProveedorService;

	@Inject
	private IUbicacionService iUbicacionService;

	public ProductosManagerBean() {
		// Constructor requerido por CDI
	}

	@PostConstruct
	public void init() {
		try {
			listaProductos();
			listaProveedores();
			listaUbicacion();
			this.listaCategorias = iCategoriaService.getAllCategorias();

			Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");

			if (user != null) {
				idUsuario = user.getIdUsuario();
				nombreUsuario = user.getNombre();
			}

			producto = new Productos();
		} catch (Exception e) {
			logger.error("Error al inicializar el bean: ", e);
		}
	}

	private void listaUbicacion() {
		try {
			this.listaUbicacion = iUbicacionService.getAll();
		} catch (Exception e) {
			logger.error("Error al obtener ubicaciones", e);
		}
	}

	private void listaProveedores() {
		try {
			this.listaProveedores = iProveedorService.proveedors();
		} catch (Exception e) {
			logger.error("Error al obtener proveedores", e);
		}
	}

	public void listaProductos() {
		try {
			this.listaProductosList = iProductoService.getAll();
		} catch (Exception e) {
			logger.error("Error al obtener productos", e);
		}
	}

	// --- Métodos de Navegación ---

	public String irATablaProductos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Tabla Productos", "entro a tabla Productos", idUsuario,
				nombreUsuario);
		return "/pages/stock_manager/productos/tablaProductos?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/stock_manager/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO.toString(), "entro a nuevo producto",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/productos/producto?faces-redirect=true";
	}

	// --- Lógica de Negocio ---

	public void guardarTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (uploadedImagen == null || uploadedImagen.getContents() == null) {
				mensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo de imagen");
				return;
			}

			producto.setArchivo(uploadedImagen.getContents());
			listaProductosGuardar.add(producto);

			// Limpiamos para el siguiente registro en la tabla temporal
			this.producto = new Productos();
			this.uploadedImagen = null;

			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA.toString(),
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);

		} catch (Exception e) {
			logger.error("Error al guardar en tabla temporal: ", e);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardarProductoTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			if (listaProductosGuardar.isEmpty()) {
				mensaje(FacesMessage.SEVERITY_WARN, "Aviso", "No hay productos en la lista para guardar");
				return;
			}
			iProductoService.create(listaProductosGuardar);
			mensaje(FacesMessage.SEVERITY_INFO, "Registro guardado", "El lote de productos fue guardado correctamente");
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR.toString(),
					Mensajes.USUARIO + nombreUsuario + " realizo el guardado masivo", Mensajes.INFO.toString(),
					idUsuario);

			this.listaProductosGuardar.clear();
			this.producto = new Productos();
			listaProductos(); // Refrescar la lista principal
		} catch (Exception e) {
			logger.error("Error en guardarProductoTabla: ", e);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void onComplete() {
		mensaje(FacesMessage.SEVERITY_INFO, "Completado", "Proceso finalizado correctamente");
	}

	public void actualizar() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			iProductoService.update(producto);
			listaProductos();
			mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Registro actualizado correctamente");

			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			logger.error("Error al actualizar: ", e);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminar() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		try {
			iProductoService.delete(producto.getIdProducto());
			listaProductos();
			mensaje(FacesMessage.SEVERITY_INFO, "Registro eliminado", "El registro fue dado de baja correctamente");

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error de validación", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.error("Error al eliminar: ", e);
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void cargaArchivos() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				mensaje(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo válido");
				return;
			}
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			mensaje(FacesMessage.SEVERITY_INFO, "Éxito", "Datos procesados y cargados a la vista.");

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error de archivo", e.getMessage());
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.error("Error en carga masiva: ", e);
			mensaje(FacesMessage.SEVERITY_ERROR, "Error interno", e.getMessage());
			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR.toString(),
					Mensajes.ERROR + ": " + e.getMessage(), Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public void aplicarFiltroExterno() {
		if (filtro != null && !filtro.trim().isEmpty()) {
			this.listaProductosList = iProductoService.getByNombreProducto(filtro);
			mensaje(FacesMessage.SEVERITY_INFO, "Resultados", "Mostrando resultados para: " + filtro);
		} else {
			listaProductos();
		}
	}
}