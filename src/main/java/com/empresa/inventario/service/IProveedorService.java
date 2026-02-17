package com.empresa.inventario.service;

import java.util.List;
import java.util.function.Consumer;

import org.primefaces.model.UploadedFile;

import com.empresa.inventario.model.Proveedor;

public interface IProveedorService {
	
	List<Proveedor> proveedors();
	void save(List<Proveedor> proveedor, Consumer<Integer> progresoCallback);
	void delete(int idProveedor);
	void update(Proveedor proveedor);
	List<Proveedor> uploadFiles(UploadedFile file);

}
