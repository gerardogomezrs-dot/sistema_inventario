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

	private MovimientosDAO dao = new MovimientosDAO();

	private ProductosDAO productosDAO = new ProductosDAO();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<Movimientos> save(List<Movimientos> movimientosL) throws Exception {
		List<Movimientos> list = new ArrayList<Movimientos>();
		
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
		 return list;
	}


	@Override
	public List<Movimientos> getAll() throws Exception {
		List<Movimientos> list = new ArrayList<Movimientos>();
		list = dao.getAll();
		try {
		if (list.size() != 0) {
			
		} else if(list.size() == 0) {
			throw new ExceptionMessage("Lista Vacia");
		}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
