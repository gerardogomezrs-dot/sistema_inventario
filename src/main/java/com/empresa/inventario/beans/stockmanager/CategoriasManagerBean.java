package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
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
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nueva Categoria", idUsuario,
				nombreUsuario);

		return "/pages/stock_manager/categorias/categorias.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaCategoria() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Tabla Categoria", "entro a tabla Categoria", idUsuario,
				nombreUsuario);
		return "/pages/stock_manager/categorias/tablaCategorias.xhtml?faces-redirect=true";
	}

	public void guardarCategoriasTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			listaTablaCategorias.add(categorias);
			this.categorias = new Categorias();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
					Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardar() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		if (listaTablaCategorias != null && !listaTablaCategorias.isEmpty()) {

			try {
				categoriaService.save(listaTablaCategorias);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro guardado", "El registro fue guardado correctamente"));
				baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
						Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro",
						Mensajes.INFO.toString(), idUsuario);
			} catch (Exception e) {
				e.printStackTrace();
				baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
						Mensajes.ERROR.toString(), idUsuario);
			}

			if (listaTablaCategorias != null) {
				listaTablaCategorias.clear();
				this.categorias = new Categorias();
			}
		}
	}

	public List<Categorias> listCategorias() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			list = categoriaService.getAllCategorias();
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
		return list;
	}

	public void actualizar() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			if (categorias != null) {
				categoriaService.update(categorias);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro actualizado", "El rgistro fue actualizado correctamente"));
				baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
						Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
						idUsuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminarCategoria() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			if (categorias != null) {
				categoriaService.delete(categorias.getIdCategoria());
				list = categoriaService.getAllCategorias();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Registro eliminado", "El registro fue eliminado correctamente"));
				baseBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
						Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(),
						idUsuario);
			} else {
				mensaje(FacesMessage.SEVERITY_WARN, "Atención", "No se seleccionó ninguna categoría.");
			}

		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}

		catch (Exception ex) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			ex.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + ex.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void cargarArchivo() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

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
					Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + ex.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}