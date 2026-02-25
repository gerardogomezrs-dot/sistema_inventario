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
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("categoriasManagerBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Categorias> filteredList;

	private List<Categorias> list;

	private Categorias categorias;

	private List<Categorias> listaTablaCategorias = new ArrayList<Categorias>();

	private transient UploadedFile uploadedFile;

	private List<Categorias> categoriasList;

	private Integer progreso = 0;

	private ICategoriaService categoriaService;
	
	private int idUsuario;
	
	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public CategoriasManagerBean(ICategoriaService categoriaService, IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
		this.categoriaService = categoriaService;
		
	}

	@PostConstruct
	public void init() {
		
			listCategorias();
			Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");
			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();
	
			categorias = new Categorias();
	}

	public String irANuevaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navega a Nueva Categoria");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/categorias/categorias.xhtml?faces-redirect=true";
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
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego tabla categoria");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public void guardarCategoriasTabla() {
		try {
			listaTablaCategorias.add(categorias);
			this.categorias = new Categorias();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Añadir registro a tabla ");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch(Exception e) {
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

	public void guardar() {
		progreso = 0;
		if (listaTablaCategorias != null && !listaTablaCategorias.isEmpty()) {
			this.progreso = 0;
			List<Categorias> copiaParaGuardar = new ArrayList<>(this.listaTablaCategorias);

			if (copiaParaGuardar.isEmpty()) {
				return;
			}
			CompletableFuture.runAsync(() -> {
				try {
					categoriaService.save(copiaParaGuardar, (valor) -> {
						this.progreso = valor;
					});
				} catch (Exception e) {
					e.printStackTrace();
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
		}
		if (listaTablaCategorias != null) {
			listaTablaCategorias.clear();
		}
		this.categorias = new Categorias();
		
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo un guardado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public List<Categorias> listCategorias() {
		list = categoriaService.getAllCategorias();
		return list;
	}

	public void actualizar() {
		try {
		if (categorias != null) {
			categoriaService.update(categorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro actualizado", "El rgistro fue actualizado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		}
		}catch (Exception e) {
			e.printStackTrace();
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

	public void eliminarCategoria() {
		try {
			if (categorias != null) {
				categoriaService.delete(categorias.getIdCategoria());
				list = categoriaService.getAllCategorias();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro eliminado", "El registro fue eliminado correctamente"));
				Auditoria auditoria = new Auditoria();
				auditoria.setFechaAuditoria(new Date());
				auditoria.setIdUsuario(idUsuario);
				auditoria.setClaseOrigen(this.getClass().getName());
				auditoria.setMetodo("Eliminar");
				auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una eliminación");
				auditoria.setNivel("INFO");
				auditoriaService.registroAuditoria(auditoria);
			} else {
				añadirMensaje(FacesMessage.SEVERITY_WARN, "Atención", "No se seleccionó ninguna categoría.");
			}

		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}

		catch (Exception e) {
			añadirMensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
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

	public void cargarArchivo() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaTablaCategorias = new ArrayList<Categorias>();
			listaTablaCategorias = categoriaService.cargarArchivo(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
		} catch (ExceptionMessage e) {
			añadirMensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.printStackTrace();
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

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}