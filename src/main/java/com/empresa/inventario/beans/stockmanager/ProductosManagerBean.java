package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.service.IProductoService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("productosManagerBean")
@javax.faces.view.ViewScoped
@Data
public class ProductosManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Categorias> listaCategorias;
	private List<Productos> listaProductosGuardar = new ArrayList<>();
	private List<Productos> listaProductosList;
	private transient UploadedFile uploadedFile;
	private Productos producto;
	private Integer progreso = 0;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	private IProductoService iProductoService;

	private ICategoriaService iCategoriaService;

	@Inject
	public ProductosManagerBean(IAuditoriaService auditoriaService, IProductoService iProductoService,
			ICategoriaService iCategoriaService) {
		this.auditoriaService = auditoriaService;
		this.iProductoService = iProductoService;
		this.iCategoriaService = iCategoriaService;
	}

	@PostConstruct
	public void init() {
		listaProductos();
		listaCategorias = iCategoriaService.getAllCategorias();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		producto = new Productos();
	}

	public void listaProductos() {
		this.listaProductosList = iProductoService.getAll();
	}

	public String irATablaProductos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a nuevo producto");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/productos/tablaProductos?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a nuevo producto");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/productos/producto?faces-redirect=true";
	}

	public void guardarTabla() {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Añadir registro");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void guardarProductoTabla() {
		this.progreso = 0;
		List<Productos> listaProductos = listaProductosGuardar;

		List<Productos> copiar = new ArrayList<>(listaProductos);
		if (copiar.isEmpty()) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			try {
				iProductoService.create(copiar, valor -> this.progreso = valor);
			} catch (Exception e) {
				e.getMessage();
				Auditoria auditoria = new Auditoria();
				auditoria.setFechaAuditoria(new Date());
				auditoria.setIdUsuario(idUsuario);
				auditoria.setClaseOrigen(this.getClass().getName());
				auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
				auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
				auditoria.setNivel(String.valueOf(Mensajes.ERROR));
				auditoriaService.registroAuditoria(auditoria);
			}
		});

		this.listaProductosGuardar.clear();
		this.producto = new Productos();
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo el guardado de registros");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		this.listaProductosGuardar.clear();
		this.producto = new Productos();
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void actualizar() {
		try {
			iProductoService.update(producto);
			listaProductos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro actualizado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void eliminar() {
		try {
			iProductoService.delete(producto.getIdProducto());
			listaProductosList = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro eliminado", "El registro fue dado de correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Eliminar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una eliminación");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void cargaArchivos() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<>();
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Carga Masiva");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
