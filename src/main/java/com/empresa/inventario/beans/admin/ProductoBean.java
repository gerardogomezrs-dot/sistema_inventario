package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.sql.SQLException;
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

import lombok.Data;

@Named("productoBean")
@javax.faces.view.ViewScoped
@Data
public class ProductoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Categorias> listaCategorias;

	private List<Productos> listaProductosGuardar = new ArrayList<Productos>();

	private List<Productos> list;

	private UploadedFile uploadedFile;

	private Productos producto;
	
	private Integer progreso = 0;


	@Inject
	private IProductoService iProductoService;

	@Inject
	private ICategoriaService iCategoriaService;
	
	private int idUsuario;

	private String nombreUsuario;
	
	@Inject
	private IAuditoriaService auditoriaService;

	public ProductoBean() {
		producto = new Productos();
	}

	@PostConstruct
	public void init() {
		try {
			ListaProductos();
			listaCategorias = iCategoriaService.getAllCategorias();
			Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");
			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardarTabla() {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Añadir registto");
		auditoria.setAccion("El usuario " + nombreUsuario + " registro un elemento a la tabla");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void guardarProductoTabla() throws Exception {
		this.progreso = 0;
		List<Productos> listaProductos = listaProductosGuardar;

		List<Productos> copiar = new ArrayList<Productos>(listaProductos);
		if(copiar.isEmpty()) {
			return;
		}
		CompletableFuture.runAsync(() -> {
			try {
				iProductoService.create(copiar, (valor) ->{
					this.progreso = valor;
				});
			}catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		this.listaProductosGuardar.clear();
		this.producto = new Productos();
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo el guardado de registros");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}
	
	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardad", "El registro fue guardado correctamente"));
	}


	public void guardarCambios() throws Exception {
		try {		
			iProductoService.update(producto);
			ListaProductos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro actualizado correctamente"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Actualizar");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo una actualización");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void eliminar() {
		try {
			iProductoService.delete(producto.getIdProducto());
			list = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro eliminado", "El Registro fue eliminado correctamente"));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Eliminar");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo la eliminación de un registro");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void ListaProductos() throws Exception {
		this.list = iProductoService.getAll();
	}

	public void cargaArchivos() throws Exception {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<Productos>();
			System.out.println("Nombre archivo: " + uploadedFile.getFileName());
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Carga Masiba");
			auditoria.setAccion("El usuario " + nombreUsuario + " realizo una carga masiva de registros");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Error");
			auditoria.setAccion("Error " + e.getMessage());
			auditoria.setNivel("WARN");
			auditoriaService.registroAuditoria(auditoria);
			
		}
	}
	
	public String irATablaProductos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navego");
		auditoria.setAccion("El usuario " + nombreUsuario +  " navego a Tabla productos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/productos/tablaProductos?faces-redirect=true";
	}

	public String menuPrincipal() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navego");
		auditoria.setAccion("El usuario " + nombreUsuario +  " navego a Dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public String irANuevoProducto() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navego");
		auditoria.setAccion("El usuario " + nombreUsuario +  " navego a nuevo producto");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/productos/productos.xhtml?faces-redirect=true";
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
