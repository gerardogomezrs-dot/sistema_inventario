package com.empresa.inventario.beans.almacen;

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
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;

import lombok.Data;

@Named("categoriasAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasAlmacenBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Categorias> filteredList;

	private List<Categorias> list;

	private Categorias categorias;

	private List<Categorias> listaTablaCategorias = new ArrayList<Categorias>();

	private UploadedFile uploadedFile;

	private List<Categorias> categoriasList;

	private Integer progreso = 0;

	@Inject
	private ICategoriaService categoriaService;
	
	private int idUsuario;
	
	private String nombreUsuario;

	@Inject
	private IAuditoriaService auditoriaService;

	public CategoriasAlmacenBean() {
		categorias = new Categorias();
	}

	@PostConstruct
	public void init() {
		try {
			listCategorias();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irANuevaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navega a Nueva Categoria");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego tabla categoria");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/almacen/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public void guardarCategoriasTabla() {
		if (this.categorias != null) {
			listaTablaCategorias.add(categorias);
			this.categorias = new Categorias();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Añadir registro a tabla ");
			auditoria.setAccion("El usuario " + nombreUsuario + " registro un elemento a la tabla");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} else {
			throw new ExceptionMessage("Ingresa valores");
		}
	}

	public void guardar() throws Exception {
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
					auditoria.setMetodo("Error");
					auditoria.setAccion("Error " + e.getMessage());
					auditoria.setNivel("WARN");
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
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo un guardado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public List<Categorias> listCategorias() throws Exception {
		return list = categoriaService.getAllCategorias();
	}

	public void actualizar() throws Exception {
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
			auditoria.setAccion("El usuario " + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		}
		}catch (Exception e) {
			e.printStackTrace();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Error");
			auditoria.setAccion("ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
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
				auditoria.setAccion("El usuario " + nombreUsuario + " realizo una eliminación");
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
			auditoria.setMetodo("Error");
			auditoria.setAccion("ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
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
			auditoria.setMetodo("Error");
			auditoria.setAccion("ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
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
			auditoria.setMetodo("Error");
			auditoria.setAccion("ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.printStackTrace();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Error");
			auditoria.setAccion("ERROR " + e.getMessage());
			auditoria.setNivel("WARN");
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}