package com.empresa.inventario.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;

import com.empresa.inventario.dao.ProveedorDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Proveedor;
import com.opencsv.CSVReader;

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
	public void save(List<Proveedor> proveedor, Consumer<Integer> progresoCallback) {
		try {
			if (proveedor == null || proveedor.isEmpty()) {
				throw new ExceptionMessage("Lista vacia");
			}
			int total = proveedor.size();
			int batchSize = 50; 
			for(int i = 0; i<total; i++) {
				dao.guardar(proveedor.get(i));
				if (i % batchSize == 0 || i == total - 1) {
		            int porcentaje = (int) (((double) (i + 1) / total) * 100);
		            progresoCallback.accept(porcentaje);
				}
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

	@Override
	public List<Proveedor> uploadFiles(UploadedFile file) {
		List<Proveedor> proveedors = new ArrayList<Proveedor>();
		String fileName = "";
		fileName = file.getFileName().toLowerCase();

		if (fileName.endsWith(".csv")) {
			try {
				proveedors = leerCSV(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (fileName.endsWith(".xlsx") || fileName.endsWith(".lsx")) {
			try {
				proveedors = leerExcel(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".lsx") && !fileName.endsWith(".csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}
		return proveedors;
	}

	private List<Proveedor> leerExcel(UploadedFile file) throws IOException {
		List<Proveedor> proveedor = new ArrayList<Proveedor>();
		Proveedor proveedores;
		Workbook workbook = WorkbookFactory.create(file.getInputstream());
		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			Cell cellNombreEmoresa = row.getCell(0);
			Cell cellContactoEmpresa = row.getCell(1);
			Cell cellTelefono = row.getCell(2);
			Cell cellEmail = row.getCell(3);
			Cell cellDireccion = row.getCell(4);
			Cell cellActivo = row.getCell(5);

			String nombreEmpresa = cellNombreEmoresa.getStringCellValue();
			String contactoEmoresa = cellContactoEmpresa.getStringCellValue();
			String telefono = cellTelefono.getStringCellValue();
			String email = cellEmail.getStringCellValue();
			String direccion = cellDireccion.getStringCellValue();
			Boolean activo = cellActivo.getBooleanCellValue();

			proveedores = new Proveedor();
			proveedores.setNombreEmpresa(nombreEmpresa);
			proveedores.setContactoEmpresa(contactoEmoresa);
			proveedores.setTelefono(telefono);
			proveedores.setEmail(email);
			proveedores.setDireccion(direccion);
			proveedores.setActivo(activo);
			proveedor.add(proveedores);
		}
		workbook.close();
		return proveedor;
	}

	private List<Proveedor> leerCSV(UploadedFile file) {
		List<Proveedor> proveedor = new ArrayList<Proveedor>();
		Proveedor prov;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputstream()))) {
			csvReader.readNext();
			String[] fila;
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {
					prov = new Proveedor();
					prov.setNombreEmpresa(fila[0]);
					prov.setContactoEmpresa(fila[1]);
					prov.setTelefono(fila[2]);
					prov.setEmail(fila[3]);
					prov.setDireccion(fila[4]);
					prov.setActivo(Boolean.parseBoolean(fila[5]));
					proveedor.add(prov);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proveedor;
	}
}
