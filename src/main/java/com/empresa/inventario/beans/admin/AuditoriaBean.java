package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.ArrayList;
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
public class AuditoriaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private List<Auditoria> filteredList;
	private List<Auditoria> list;
	
	@Inject
	private IAuditoriaService auditoriaService;
	
	public AuditoriaBean() {
		
	}
	
	@PostConstruct
	public void init() {
		try {
		cargarListaAuditoria();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cargarListaAuditoria() {
		list = new ArrayList<Auditoria>();
		if(list !=null || !list.isEmpty()) {
		list = auditoriaService.getAll();
		}
	}
}
