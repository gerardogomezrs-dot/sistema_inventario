package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.Categorias;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.ICategoriaService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("categoriasAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class CategoriasAlmacenBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String filtro;

	private List<Categorias> list;

	private List<Categorias> filteredList;

	private ICategoriaService categoriaServicio;

	private transient UploadedFile uploadedFile;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public CategoriasAlmacenBean(ICategoriaService categoriaService, IAuditoriaService auditoriaService) {
		this.categoriaServicio = categoriaService;
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		cargarListaCategorias();
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = usuario.getIdUsuario();
		nombreUsuario = usuario.getNombre();
	}

	public void cargarListaCategorias() {
		try {
			list = categoriaServicio.getAllCategorias();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String irADashboard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NAVEGACION, "navego a dashboard", idUsuario,
				nombreUsuario);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public void aplicarFiltroExterno() {
		if (filtro != null && !filtro.trim().isEmpty()) {
			this.list = categoriaServicio.byNombreCategoria(filtro);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultados", "Mostrando resultados para: " + filtro));
		} else {
			this.list = categoriaServicio.getAllCategorias();
		}
	}
}
