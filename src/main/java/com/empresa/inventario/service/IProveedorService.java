package com.empresa.inventario.service;

import java.util.List;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Proveedor;

public interface IProveedorService {
	
	List<Proveedor> proveedors();
	void save(List<Proveedor> proveedor);
	void delete(int idProveedor);
	void update(Proveedor proveedor);
	List<Proveedor> uploadFiles(UploadedFile file);

}
