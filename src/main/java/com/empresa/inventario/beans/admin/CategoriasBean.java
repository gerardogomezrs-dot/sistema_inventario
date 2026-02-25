package com.empresa.inventario.beans.admin;

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

@Named("categoriasBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient List<Categorias> filteredList;
	private transient List<Categorias> list;
	private  Categorias categorias;
	private transient List<Categorias> listaTablaCategorias = new ArrayList<Categorias>();

	private transient UploadedFile uploadedFile;

	private transient List<Categorias> categoriasList;

	private Integer progreso = 0;
	
	
	private ICategoriaService categoriaService;

	private int idUsuario;

	private String nombreUsuario;
	
	private String user="El usuario ";

	
	private IAuditoriaService auditoriaService;

	@Inject
	public CategoriasBean(ICategoriaService categoriaService, IAuditoriaService auditoriaService) {
		this.categoriaService = categoriaService;
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
			listaCategorias();
			list = categoriaService.getAllCategorias();
			Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");
			idUsuario = usuario.getIdUsuario();
			nombreUsuario = usuario.getNombre();
			categorias = new Categorias();
	}

	public void listaTabla() {
		if (this.categorias != null) {
			listaTablaCategorias.add(this.categorias);
			this.categorias = new Categorias();
		}
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Añadir registro a tabla ");
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla");
		auditoria.setNivel(Mensajes.INFO.toString());
		auditoriaService.registroAuditoria(auditoria);
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
					categoriaService.save(copiaParaGuardar, valor -> {
						this.progreso = valor;
					});

				} catch (Exception e) {
					e.printStackTrace();
					Auditoria auditoria = new Auditoria();
					auditoria.setFechaAuditoria(new Date());
					auditoria.setIdUsuario(idUsuario);
					auditoria.setClaseOrigen(this.getClass().getName());
					auditoria.setMetodo(Mensajes.ERROR.toString());
					auditoria.setAccion(Mensajes.ERROR.toString() + e.getMessage());
					auditoria.setNivel(Mensajes.ERROR.toString());
					auditoriaService.registroAuditoria(auditoria);
				}
			});
		}

		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Guardar");
		auditoria.setAccion(user + nombreUsuario + " realizo un guardado");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);

		listaTablaCategorias.clear();

		this.categorias = new Categorias();
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void actualizar() {
		try {
			categoriaService.update(categorias);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro actualizado", "El registro fue actualizado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizar");
			auditoria.setAccion(user + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
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

	public List<Categorias> listaCategorias() {
		list = categoriaService.getAllCategorias();
		return list;
	}

	public void eliminarCategoria() {
		try {
			if (categorias != null) {
				categoriaService.delete(categorias.getIdCategoria());
				list = categoriaService.getAllCategorias();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro eliminado", "El registro fue eliminado correctamente"));
			}
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Eliminar");
			auditoria.setAccion(user + nombreUsuario + " realizo una eliminación");
			auditoria.setNivel(String.valueOf(Mensajes.INFO));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
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
			return;
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

			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Carga masiva de archivos");
			auditoria.setAccion(user + nombreUsuario + " realizo una carga masiva de registros");
			auditoria.setNivel(String.valueOf(Mensajes.INFO));
			auditoriaService.registroAuditoria(auditoria);

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
		}
	}

	public String irANuevaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegación");
		auditoria.setAccion(user + nombreUsuario + " navega a Nueva Categoria");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(user + nombreUsuario + " navego tabla categoria");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(user + nombreUsuario + " navego a dashboard");
		auditoria.setNivel(String.valueOf(Mensajes.INFO));
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
