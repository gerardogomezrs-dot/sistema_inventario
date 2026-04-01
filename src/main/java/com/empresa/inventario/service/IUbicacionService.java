package com.empresa.inventario.service;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Ubicacion;

public interface IUbicacionService {
	
	public List<Ubicacion> getAll();
	
	public void create(List<Ubicacion> ubicacion);
	
	public void delete(int idUsuario);
	
	public void update(Ubicacion ubicacion);
	
	List<Ubicacion> cargarArchivo(UploadedFile uploadedFile);

}
