package com.empresa.inventario.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.utils.CrearCodigoBarra;

@Named("productoService")
@ApplicationScoped
public class ProductosServiceImp implements IProductoService {

	private ProductosDAO productosDAO;

	@Override
	public void delete(int idProducto) throws Exception {
		if (idProducto == 0) {
			throw new ExceptionMessage("Sin Datos");
		} else {
			productosDAO.eliminarProducto(idProducto);
		}
	}

	@Override
	public List<Productos> create(List<Productos> productosLista) throws Exception {
		for(Productos productos: productosLista) {
		try {
			if (productos == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				productos.setCodigoBarras(CrearCodigoBarra.generarCodigoBarra(productos.getCodigoBarras()));
				ProductosDAO dao = new ProductosDAO();
				dao.guardar(productos);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
		}
		return productosLista;

	}

	@Override
	public List<Productos> getAll() throws Exception {
		List<Productos> getProductos = new ArrayList<>();
		productosDAO = new ProductosDAO();
		getProductos = productosDAO.getAll();
		if (getProductos.size() == 0) {
			throw new ExceptionMessage("Vacio");
		} else {
			getProductos = productosDAO.getAll();
		}

		return getProductos;

	}

	@Override
	public void update(Productos productos) throws Exception {
		try {
			if (productos == null) {
				throw new ExceptionMessage("Vacio");
			} else {

				ProductosDAO dao = new ProductosDAO();
				dao.actualizar(productos);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

}
