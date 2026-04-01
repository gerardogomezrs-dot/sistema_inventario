package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.UbicacionDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Ubicacion;
import com.opencsv.CSVReader;

@Named("ubicacionService")
@ApplicationScoped
public class UbicacionServiceImpl implements IUbicacionService {

	private UbicacionDAO ubicacionDAO = new UbicacionDAO();

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UbicacionServiceImpl.class);

	@Override
	public List<Ubicacion> getAll() {
		List<Ubicacion> ubicacions = new ArrayList<>();
		ubicacions = ubicacionDAO.getAll();
		return ubicacions;
	}

	@Override
	public void create(List<Ubicacion> ubicacion) {
		try {
			for (Ubicacion ubicacion2 : ubicacion) {
				ubicacionDAO.guardar(ubicacion2);
			}
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

	@Override
	public List<Ubicacion> cargarArchivo(UploadedFile uploadedFile) {
		List<Ubicacion> p = new ArrayList<>();
		if (uploadedFile.getFileName() == null || uploadedFile.getFileName().trim().isEmpty()) {
			throw new ExceptionMessage("Inserta un archivo");
		}
		String fileName = "";
		fileName = uploadedFile.getFileName().toLowerCase();

		if (fileName.endsWith(".csv")) {
			try {
				p = leerCSV(uploadedFile);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		if (fileName.endsWith("xlsx") || fileName.endsWith("lsx")) {
			try {
				p = leerExcel(uploadedFile);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".lsx") && !fileName.endsWith(".csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}

		return p;

	}

	private List<Ubicacion> leerExcel(UploadedFile uploadedFile) {
	    List<Ubicacion> ubicacionLista = new ArrayList<>();

	    try (java.io.InputStream is = uploadedFile.getInputstream();
	         Workbook workbook = WorkbookFactory.create(is)) {  // ← ambos en try-with-resources

	        Sheet sheet = workbook.getSheetAt(0);
	        DataFormatter dataFormatter = new DataFormatter();
	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue;
	            Cell cellPasillo = row.getCell(0);
	            Cell cellEstante = row.getCell(1);
	            Cell cellNivel = row.getCell(2);
	            Cell cellCodigoControl = row.getCell(3);
	            Cell cellEstado = row.getCell(4);

	            Ubicacion ubicacion = new Ubicacion();
	            ubicacion.setPasillo(dataFormatter.formatCellValue(cellPasillo).toUpperCase());
	            ubicacion.setEstante(dataFormatter.formatCellValue(cellEstante).toUpperCase());
	            ubicacion.setNivel(dataFormatter.formatCellValue(cellNivel).toUpperCase());
	            ubicacion.setCodigoControl(dataFormatter.formatCellValue(cellCodigoControl));
	            ubicacion.setEstado(dataFormatter.formatCellValue(cellEstado));

	            ubicacionLista.add(ubicacion);
	        }

	    } catch (Exception e) {
	        logger.error("Error al leer archivo Excel: " + e.getMessage(), e);
	    }

	    return ubicacionLista;
	}

	private List<Ubicacion> leerCSV(UploadedFile uploadedFile) {
		List<Ubicacion> ubicacionLista = new ArrayList<>();

		try (CSVReader csvReader = new CSVReader(new InputStreamReader(uploadedFile.getInputstream()))) {
			String[] fila;
			csvReader.readNext();
			while ((fila = csvReader.readNext()) != null) {

				if (fila.length >= 5) {
					Ubicacion ubicacion = new Ubicacion();

					ubicacion.setPasillo(fila[0].trim().toUpperCase());
					ubicacion.setEstante(fila[1].trim().toUpperCase());
					ubicacion.setNivel(fila[2].trim().toUpperCase());
					ubicacion.setCodigoControl(fila[3].trim());
					ubicacion.setEstado(fila[4].trim());

					ubicacionLista.add(ubicacion);

				} else {
				}
			}
		} catch (Exception e) {
			logger.error("Error al leer archivo CSV: " + e.getMessage(), e);
		}
		return ubicacionLista;
	}
}
