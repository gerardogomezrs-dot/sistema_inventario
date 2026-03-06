package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Usuario;

public interface IMermasDevolucionesService {

	List<MermasDevoluciones> getListaMermasDevoluciones();
	
	void guardarMermasDevoluciones(List<MermasDevoluciones> devoluciones, Usuario usuario);
	
}
