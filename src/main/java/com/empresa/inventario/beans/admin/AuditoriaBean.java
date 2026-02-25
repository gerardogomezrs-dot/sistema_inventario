package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.service.IAuditoriaService;

import lombok.Data;

@Named("auditoriaBean")
@javax.faces.view.ViewScoped
@Data
public class AuditoriaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient List<Auditoria> filteredList;
	private transient List<Auditoria> list;

	private IAuditoriaService auditoriaService;

	@Inject
	public AuditoriaBean(IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	@PostConstruct
	public void init() {
		cargarListaAuditoria();
	}

	public void cargarListaAuditoria() {
		list = auditoriaService.getAll();
	}

	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}
}
