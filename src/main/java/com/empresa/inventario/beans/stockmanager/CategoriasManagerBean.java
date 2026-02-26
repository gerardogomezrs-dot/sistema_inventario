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

	private List<Categorias> listaTablaCategorias = new ArrayList<>();

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
		auditoria.setMetodo(Mensajes.NAVEGACION.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navega a Nueva Categoria");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.NAVEGACION.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a dashboard");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(Mensajes.NAVEGACION.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego tabla categoria");
		auditoria.setNivel(Mensajes.INFO.toString());
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
			auditoria.setMetodo(Mensajes.GUARDAR_REGISTRO_TABLA.toString());
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla");
			auditoria.setNivel(Mensajes.INFO.toString());
			auditoriaService.registroAuditoria(auditoria);
		} catch(Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR.toString() + e.getMessage());
			auditoria.setNivel(Mensajes.WARN.toString());
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
					categoriaService.save(copiaParaGuardar, valor -> this.progreso = valor);
				} catch (Exception e) {
					e.getMessage();
					Auditoria auditoria = new Auditoria();
					auditoria.setFechaAuditoria(new Date());
					auditoria.setIdUsuario(idUsuario);
					auditoria.setClaseOrigen(this.getClass().getName());
					auditoria.setMetodo(Mensajes.ERROR.toString());
					auditoria.setAccion(Mensajes.ERROR + e.getMessage());
					auditoria.setNivel(Mensajes.WARN.toString());
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
		auditoria.setMetodo(Mensajes.GUARDAR.toString());
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una guardado");
		auditoria.setNivel(Mensajes.INFO.toString());
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
			auditoria.setMetodo(Mensajes.ACTUALIZAR.toString());
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una actualización");
			auditoria.setNivel(Mensajes.INFO.toString());
			auditoriaService.registroAuditoria(auditoria);
		}
		}catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.INFO.toString());
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
				auditoria.setMetodo(Mensajes.ELIMINAR.toString());
				auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una eliminación");
				auditoria.setNivel(Mensajes.INFO.toString());
				auditoriaService.registroAuditoria(auditoria);
			} else {
				mensaje(FacesMessage.SEVERITY_WARN, "Atención", "No se seleccionó ninguna categoría.");
			}

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
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.WARN.toString());
			auditoriaService.registroAuditoria(auditoria);
		}

		catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.WARN.toString());
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void cargarArchivo() {
		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaTablaCategorias = new ArrayList<>();
			listaTablaCategorias = categoriaService.cargarArchivo(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.CARGA_MASIVA_REGISTROS.toString());
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + "realizo la carga masiva de registros");
			auditoria.setNivel(Mensajes.INFO.toString());
			auditoriaService.registroAuditoria(auditoria);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.WARN.getTexto());
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.ERROR.toString());
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}