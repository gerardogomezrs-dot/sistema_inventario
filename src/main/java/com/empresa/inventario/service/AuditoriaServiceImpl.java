package com.empresa.inventario.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.AuditoriaDao;
import com.empresa.inventario.model.Auditoria;

@Named("auditoriaService")
@ApplicationScoped
public class AuditoriaServiceImpl implements IAuditoriaService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private AuditoriaDao auditoriaDao;

	@Override
	public void registroAuditoria(Auditoria auditoria) {	
		try {
			auditoriaDao = new AuditoriaDao();
			auditoriaDao.guardar(auditoria);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public List<Auditoria> getAll() {
		List<Auditoria> auditorias = new ArrayList<Auditoria>();
		try {
			auditoriaDao = new AuditoriaDao();
			auditorias = auditoriaDao.getAllAuditoria();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return auditorias;
	}
	
}
