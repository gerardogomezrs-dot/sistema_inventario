package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

	private Categorias cat;

	@Override
	public void save(List<Categorias> list) throws Exception {
		try {
			for (Categorias categorias : list) {
				if (categorias == null) {
					throw new ExceptionMessage("Vacio");
				} else {
					dao.guardar(categorias);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			if (idCategoria == 0) {
				throw new ExceptionMessage("Ingrese el id");
			} else {
				dao.eliminarCategoria(idCategoria);
			}
		} catch (Exception e) {
			throw new ExceptionMessage("No se puede eliminar la categoria");
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
		if (!fileName.endsWith("xlsx") || !fileName.endsWith("lsx") || !fileName.endsWith("csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}
		return categorias;
	}

	private List<Categorias> leerExcel(UploadedFile file) throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		Workbook workbook = WorkbookFactory.create(file.getInputstream());
		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;
			Cell cellNombre = row.getCell(0);
			Cell cellDescripcion = row.getCell(1);

			String nombreCategoria = cellNombre.getStringCellValue();
			String descripcionCategoria = cellDescripcion.getStringCellValue().toString();
			cat = new Categorias();
			cat.setNombre(nombreCategoria);
			cat.setDescripcion(descripcionCategoria);
			categorias.add(cat);
		}
		workbook.close();
		return categorias;
	}

	private List<Categorias> leerCSV(UploadedFile file) throws Exception {
		List<Categorias> categorias = new ArrayList<Categorias>();
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputstream()))) {
			csvReader.readNext();
			String[] fila;
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {
					cat = new Categorias();
					cat.setNombre(fila[0]);
					cat.setDescripcion(fila[1]);
					categorias.add(cat);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categorias;
	}

	private void añadirMensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

}
