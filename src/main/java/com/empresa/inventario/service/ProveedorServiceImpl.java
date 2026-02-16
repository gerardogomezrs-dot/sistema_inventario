package com.empresa.inventario.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.ProveedorDAO;
import com.empresa.inventario.model.Proveedor;

@Named("proveedorService")
@ApplicationScoped
public class ProveedorServiceImpl implements Serializable, IProveedorService {

	private static final long serialVersionUID = 1L;
	private ProveedorDAO dao;

	@Override
	public List<Proveedor> proveedors() {
		List<Proveedor> proveedors = new ArrayList<Proveedor>();
		dao = new ProveedorDAO();
		try {
			proveedors = dao.getAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proveedors;
	}

	@Override
	public void save(List<Proveedor> proveedor) {
		try {
			for (Proveedor pro : proveedor) {
				dao = new ProveedorDAO();
				dao.guardar(pro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int idProveedor) {
		try {
			dao.eliminarProveedor(idProveedor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Proveedor proveedor) {
		try {
			dao.update(proveedor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
