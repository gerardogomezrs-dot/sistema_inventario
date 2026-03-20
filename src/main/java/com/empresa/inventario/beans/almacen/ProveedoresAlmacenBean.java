package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.Proveedor;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IProveedorService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("proveedoresAlmacenBean")
@javax.faces.view.ViewScoped
@Data
public class ProveedoresAlmacenBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private transient List<Proveedor> list;

	private IProveedorService iProveedorService;

	private transient List<Proveedor> filteredList;

	private String filtro;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public ProveedoresAlmacenBean(IProveedorService iProveedorService, IAuditoriaService auditoriaService) {
		this.iProveedorService = iProveedorService;
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		listaProveedores();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	public void listaProveedores() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			list = iProveedorService.proveedors();
		} catch (Exception e) {
			e.printStackTrace();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
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
			this.list = iProveedorService.byNombreEmpresa(filtro);
			System.err.println("Recibo nombre del producto " + filtro);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultados", "Mostrando resultados para: " + filtro));
		} else {
			this.list = iProveedorService.proveedors();
		}
	}

}
