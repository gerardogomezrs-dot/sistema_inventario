package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.AuditoriaDao;
import com.empresa.inventario.model.Auditoria;

@Named("auditoriaService")
@ApplicationScoped
public class AuditoriaServiceImpl implements IAuditoriaService {
	
	private  AuditoriaDao auditoriaDao;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuditoriaServiceImpl.class);
	
	@Override
	public void registroAuditoria(Auditoria auditoria) {
		try {
			auditoriaDao = new AuditoriaDao();
			auditoriaDao.guardar(auditoria);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	@Override
	public List<Auditoria> getAll() {
		List<Auditoria> auditorias = new ArrayList<>();
		try {
			auditoriaDao = new AuditoriaDao();
			auditorias = auditoriaDao.getAllAuditoria();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return auditorias;
	}
}
