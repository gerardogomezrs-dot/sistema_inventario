package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Movimientos;

public interface IMovimientosService {

	List<Movimientos> save(List<Movimientos> movimientos);
	List<Movimientos> getAll() ;
}
