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

		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nuevo producto", idUsuario,
				nombreUsuario);

		return "/pages/stock_manager/productos/producto?faces-redirect=true";
	}

	public void guardarTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
		listaProductosGuardar.add(producto);
		this.producto = new Productos();
		baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR_REGISTRO_TABLA,
				Mensajes.USUARIO + nombreUsuario + " registro un elemento a la tabla", Mensajes.INFO.toString(),
				idUsuario);
		}catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void guardarProductoTabla() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();


		try {
			iProductoService.create(listaProductosGuardar);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro guardado", "El registro fue guardado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}

		this.listaProductosGuardar.clear();
		this.producto = new Productos();
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro guardado", "El registro fue guardado correctamente"));
	}

	public void actualizar() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			iProductoService.update(producto);
			listaProductos();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Registro actualizado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			e.getMessage();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminar() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		try {
			iProductoService.delete(producto.getIdProducto());
			listaProductosList = iProductoService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Registro eliminado", "El registro fue dado de correctamente"));

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.getMessage();

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			e.getMessage();

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void cargaArchivos() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

		try {
			if (uploadedFile == null || uploadedFile.getContents() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Seleccione un archivo"));
			}
			listaProductosGuardar = new ArrayList<>();
			listaProductosGuardar = iProductoService.cargaArchivos(uploadedFile);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Datos cargados a la tabla."));

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.CARGA_MASIVA_REGISTROS.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una carga masiva de registros",
					Mensajes.INFO.toString(), idUsuario);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());

			auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
