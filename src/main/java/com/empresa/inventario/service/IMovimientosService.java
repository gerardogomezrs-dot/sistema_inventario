package com.empresa.inventario.service;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Movimientos;

public interface IMovimientosService {

	List<Movimientos> save(List<Movimientos> movimientos);
	List<Movimientos> getAll() ;
	List<Movimientos> getbyIdUsuarioMovimientos(int idUsuario);
	List<Movimientos> cargaMasiva(UploadedFile file, int idUsuario);

}
