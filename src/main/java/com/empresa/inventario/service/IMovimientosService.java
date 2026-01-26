package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.Movimientos;

public interface IMovimientosService {

	void save(Movimientos movimientos) throws Exception;
	void update(Movimientos movimientos) throws Exception;
	void delete(int idMovimiento) throws Exception;
	List<Movimientos> getAll() throws Exception;
}
