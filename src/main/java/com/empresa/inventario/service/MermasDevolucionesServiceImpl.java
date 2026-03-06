package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.MermasDevolucionesDAO;
import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;

@Named("mermasDevolucionesService")
@ApplicationScoped
public class MermasDevolucionesServiceImpl implements IMermasDevolucionesService {


	@Override
	public List<MermasDevoluciones> getListaMermasDevoluciones() {
		List<MermasDevoluciones> devoluciones = new ArrayList<>();
		MermasDevolucionesDAO devolucionesDAO = new MermasDevolucionesDAO();
		try {
			devoluciones = devolucionesDAO.getAllMermasDevoluciones();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devoluciones;
	}

	@Override
	public void guardarMermasDevoluciones(List<MermasDevoluciones> devoluciones, Usuario usuario) {
		MermasDevolucionesDAO devolucionesDAO = new MermasDevolucionesDAO();
		List<MermasDevoluciones> mermasDevoluciones = new ArrayList<>(devoluciones);
		ProductosDAO dao = new ProductosDAO();
		Productos productos = new Productos();
		try {
			for (MermasDevoluciones mermasDevoluciones2 : mermasDevoluciones) {
				
				System.err.println("Tipo " + mermasDevoluciones2.getTipo());
				
				mermasDevoluciones2.setIdUsuario(usuario.getIdUsuario());
				if (mermasDevoluciones2.getTipo().equals("Merma")) {
					System.err.println("Merma");
					productos = dao.getByIdProductoInfo(mermasDevoluciones2.getIdProducto());
					double costo = productos.getPrecioUnitario() * mermasDevoluciones2.getCantidad();
					System.err.println("Costo " + costo+ "precio unitario " + productos.getPrecioUnitario());
					mermasDevoluciones2.setCostoPerdido(costo);
					devolucionesDAO.guardar(mermasDevoluciones2);
					int stockActual = productos.getStockActual() - mermasDevoluciones2.getCantidad();
					dao.actualizarStock(mermasDevoluciones2.getIdProducto(), stockActual);
				}
				 if (mermasDevoluciones2.getTipo().equals("Devolucion")) {
						System.err.println("Devolucion");
					productos = dao.getByIdProductoInfo(mermasDevoluciones2.getIdProducto());
					double costo = 0;
					mermasDevoluciones2.setCostoPerdido(costo);
					devolucionesDAO.guardar(mermasDevoluciones2);
					int stockActual = productos.getStockActual() + mermasDevoluciones2.getCantidad();
					dao.actualizarStock(mermasDevoluciones2.getIdProducto(), stockActual);
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
