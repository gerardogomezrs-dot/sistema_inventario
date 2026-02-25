package com.empresa.inventario.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.MovimientosDAO;
import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Movimientos;

@Named("movimientoService")
@ApplicationScoped
public class MovimientosServiceImpl implements IMovimientosService, Serializable {

	private static final long serialVersionUID = 1L;

	
	private transient MovimientosDAO dao = new MovimientosDAO();

	private transient ProductosDAO productosDAO = new ProductosDAO();


	@Override
	public List<Movimientos> save(List<Movimientos> movimientosL) {
		List<Movimientos> list = new ArrayList<Movimientos>();
		try {
		for(Movimientos movimientos: movimientosL) {
		
		if (movimientos == null) {
			throw new ExceptionMessage("Vacio");
		}

		int totalStock = 0;
		int unidad = movimientos.getCantidad();
		
		dao.guardar(movimientos);
		if (movimientos.getTipoMovimiento().equals("Entrada")) {
			int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
			totalStock = nuevoSock + movimientos.getCantidad();
			productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
		}
		if (movimientos.getTipoMovimiento().equals("Salida")) {
			int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
			totalStock = nuevoSock - movimientos.getCantidad();
			productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
		}
		if (movimientos.getTipoMovimiento().equals("Ajuste")) {
			productosDAO.actualizarStock(movimientos.getIdProducto(), unidad);
		}
		}
		}catch (Exception e) {
		e.getMessage();
		}
		 return list;
	}


	@Override
	public List<Movimientos> getAll() {
		List<Movimientos> list = new ArrayList<Movimientos>();
		List<Movimientos> movimientos = null;
		
		try {
			list = dao.getAll();
		if (list.size() != 0) {
		 movimientos = new ArrayList<Movimientos>(list);
		} else if(list.size() == 0) {
			throw new ExceptionMessage("Lista Vacia");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return movimientos;
	}

}
