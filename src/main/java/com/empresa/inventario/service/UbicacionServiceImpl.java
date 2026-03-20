package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.UbicacionDAO;
import com.empresa.inventario.model.Ubicacion;

@Named("ubicacionService")
@ApplicationScoped
public class UbicacionServiceImpl implements IUbicacionService {

	private UbicacionDAO ubicacionDAO = new UbicacionDAO();

	@Override
	public List<Ubicacion> getAll() {
		List<Ubicacion> ubicacions = new ArrayList<>();
		ubicacions = ubicacionDAO.getAll();
		return ubicacions;
	}

	@Override
	public void create(Ubicacion ubicacion) {
		try {
			ubicacionDAO.guardar(ubicacion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int idUsuario) {
		try {
			ubicacionDAO.eliminar(idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Ubicacion ubicacion) {
		try {
			ubicacionDAO.actualizar(ubicacion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
