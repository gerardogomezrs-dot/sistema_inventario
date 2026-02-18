package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Auditoria;

public interface IAuditoriaService {
	
	void registroAuditoria(Auditoria auditoria);
	List<Auditoria> getAll();

}
