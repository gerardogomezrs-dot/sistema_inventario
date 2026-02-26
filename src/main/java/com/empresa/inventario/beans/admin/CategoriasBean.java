package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
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
	private Categorias categorias;
	private transient List<Categorias> listaTablaCategorias = new ArrayList<>();

	private transient UploadedFile uploadedFile;

	private transient List<Categorias> categoriasList;

	private Integer progreso = 0;

	private BaseBean baseBean;

	private ICategoriaService categoriaService;

	private int idUsuario;

	private String nombreUsuario;

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

		baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
				Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString());
	}

	public void guardar() {
		progreso = 0;
		listaTablaCategorias = new ArrayList<>();
		this.progreso = 0;
		List<Categorias> copiaParaGuardar = new ArrayList<>(this.listaTablaCategorias);

		CompletableFuture.runAsync(() -> {
			try {
				categoriaService.save(copiaParaGuardar, valor -> this.progreso = valor);
				baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
						Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro",
						Mensajes.INFO.toString());

			} catch (Exception e) {
				e.getMessage();
				baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
						Mensajes.ERROR.toString());
			}
		});

		if (listaTablaCategorias.isEmpty()) {
			listaTablaCategorias.clear();
			this.categorias = new Categorias();
		}
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
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString());

		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString());
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
				baseBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
						Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString());
			}

		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString());
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

			baseBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString());

		} catch (ExceptionMessage e) {
			e.getMessage();
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString());
		}
	}

	public String irANuevaCategoria() {
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nueva Categoria");
		return "/pages/admin/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		baseBean.registrarNavegacion(auditoriaService, "Tabla Categoria", "entro a tabla Categoria");
		return "/pages/admin/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard");
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
