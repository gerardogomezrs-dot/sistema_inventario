package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.AuditoriaDao;
import com.empresa.inventario.model.Auditoria;

@Named("auditoriaService")
@ApplicationScoped
public class AuditoriaServiceImpl implements IAuditoriaService {
	
	private  AuditoriaDao auditoriaDao;
	

	@Override
	public void registroAuditoria(Auditoria auditoria) {
		try {
			auditoriaDao = new AuditoriaDao();
			auditoriaDao.guardar(auditoria);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public List<Auditoria> getAll() {
		List<Auditoria> auditorias = new ArrayList<>();
		try {
			auditoriaDao = new AuditoriaDao();
			auditorias = auditoriaDao.getAllAuditoria();
		} catch (Exception e) {
			e.getMessage();
		}
		return auditorias;
	}
}
