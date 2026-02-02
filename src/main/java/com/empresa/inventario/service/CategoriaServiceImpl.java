package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;

import com.empresa.inventario.dao.CategoriasDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Categorias;
import com.opencsv.CSVReader;

@Named("categoriaService")
@ApplicationScoped
public class CategoriaServiceImpl implements ICategoriaService {

	private CategoriasDAO dao = new CategoriasDAO();

	@Override
	public void save(List<Categorias> list) throws Exception {
		for (Categorias categorias : list) {
			if (categorias == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				dao.guardar(categorias);
			}
		}
	}

	@Override
	public void update(Categorias categorias) throws Exception {
		if (categorias == null) {
			throw new ExceptionMessage("Vacio");
		} else {
			dao.actualizar(categorias);
		}

	}

	@Override
	public List<Categorias> getAllCategorias() throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		categorias = dao.getAllCategorias();
		try {
			if (categorias.size() == 0) {
				throw new ExceptionMessage("Lista Vacia");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorias;
	}

	@Override
	public void delete(int idCategoria) throws Exception {
		if (idCategoria == 0) {
			throw new ExceptionMessage("Ingrese el id");
		} else {
			dao.eliminarCategoria(idCategoria);
		}
	}

	@Override
	public List<Categorias> cargarArchivo(UploadedFile file) {
		List<Categorias> categorias = new ArrayList<Categorias>();
		String fileName = "";

		fileName = file.getFileName().toLowerCase();
		
		if (fileName.endsWith(".csv")) {
			try {
				categorias = leerCSV(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (fileName.endsWith("xlsx") || fileName.endsWith("lsx")) {
			try {
				categorias = leerExcel(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return categorias;

	}

	private List<Categorias> leerExcel(UploadedFile file) throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		Workbook workbook = WorkbookFactory.create(file.getInputstream());
		Sheet sheet = workbook.getSheetAt(0); // Primera hoja
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			Cell cellNombre = row.getCell(0);
			Cell cellDescripcion = row.getCell(1);

			String nombreCategoria = cellNombre.getStringCellValue();
			String descripcionCategoria = cellDescripcion.getStringCellValue().toString();
			categorias.add(new Categorias(0, nombreCategoria, descripcionCategoria));

		}
		workbook.close();
		return categorias;
	}

	private List<Categorias> leerCSV(UploadedFile file) throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputstream()))) {
			String[] fila;
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {
					categorias.add(new Categorias(0, fila[0], fila[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorias;

	}

}
