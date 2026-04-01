package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.MovimientosDAO;
import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Movimientos;
import com.opencsv.CSVReader;

@Named("movimientoService")
@ApplicationScoped
public class MovimientosServiceImpl implements IMovimientosService {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MovimientosServiceImpl.class);

	private MovimientosDAO dao;

	@Override
	public List<Movimientos> save(List<Movimientos> movimientosL) {
		List<Movimientos> list = new ArrayList<>();
		try {
			if (movimientosL == null) {
				throw new ExceptionMessage("Vacio");
			}
			for (Movimientos movimientos : movimientosL) {

				int totalStock = 0;
				int unidad = movimientos.getCantidad();
				
				dao = new MovimientosDAO();
				
				ProductosDAO productosDAO = new ProductosDAO();
				if (movimientos.getTipoMovimiento().equalsIgnoreCase("Entrada")) {
					System.err.println("Entrada");
					int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
					totalStock = nuevoSock + movimientos.getCantidad();
					productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
					movimientos.setStockPrevio(nuevoSock);
					movimientos.setStockPosterior(totalStock);
					dao.guardar(movimientos);
				}
				if (movimientos.getTipoMovimiento().equalsIgnoreCase("Salida")) {
					System.err.println("Salida");
					int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
					totalStock = nuevoSock - movimientos.getCantidad();
					productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
					movimientos.setStockPrevio(nuevoSock);
					movimientos.setStockPosterior(totalStock);
					dao.guardar(movimientos);
				}
				if (movimientos.getTipoMovimiento().equalsIgnoreCase("Ajuste")) {
					productosDAO.actualizarStock(movimientos.getIdProducto(), unidad);
					int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
					movimientos.setStockPrevio(nuevoSock);
					movimientos.setStockPosterior(unidad);
					dao.guardar(movimientos);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Movimientos> getAll() {
		List<Movimientos> list = new ArrayList<>();
		List<Movimientos> movimientos = null;
		try {
			dao = new MovimientosDAO();
			list = dao.getAll();
			movimientos = new ArrayList<>(list);

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return movimientos;
	}

	@Override
	public List<Movimientos> getbyIdUsuarioMovimientos(int idUsuario) {

		List<Movimientos> movimientos = new ArrayList<>();
		try {
			dao = new MovimientosDAO();
			movimientos = dao.getByUsuario(idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return movimientos;
	}

	@Override
	public List<Movimientos> cargaMasiva(UploadedFile uploadedFile, int idUsuario) {
		List<Movimientos> p = new ArrayList<>();
		if (uploadedFile.getFileName() == null || uploadedFile.getFileName().trim().isEmpty()) {
			throw new ExceptionMessage("Inserta un archivo");
		}
		String fileName = "";
		fileName = uploadedFile.getFileName().toLowerCase();

		if (fileName.endsWith(".csv")) {
			try {
				p = leerCVS(uploadedFile, idUsuario);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		if (fileName.endsWith("xlsx") || fileName.endsWith("lsx")) {
			try {
				p = leerExcel(uploadedFile, idUsuario);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".lsx") && !fileName.endsWith(".csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}
		return p;
	}

	private List<Movimientos> leerExcel(UploadedFile file, int idUsuario) {
		List<Movimientos> list = new ArrayList<>();
		Movimientos movimientos;
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputstream());
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;
				Cell cellIdProducto = row.getCell(0);
				Cell cellTipoMovimiento = row.getCell(1);
				Cell cellCantidad = row.getCell(2);
				Cell cellFechaHora = row.getCell(3);
				Cell cellOrigenDestino = row.getCell(4);
				Cell cellObservaciones = row.getCell(5);

				DataFormatter dataFormatter = new DataFormatter();

				String idProducto = dataFormatter.formatCellValue(cellIdProducto);
				int id = NumberUtils.toInt(idProducto, 0);
				String tipoMovimiento = cellTipoMovimiento.getStringCellValue();
				String cantidadString = dataFormatter.formatCellValue(cellCantidad);
				int cantidad = NumberUtils.toInt(cantidadString, 0);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fecha = LocalDateTime.parse(cellFechaHora.getStringCellValue(), formatter);
				String origenDestino = cellOrigenDestino.getStringCellValue();
				
				String observaciones = cellObservaciones.getStringCellValue();
				movimientos = new Movimientos();
				movimientos.setIdProducto(id);
				movimientos.setTipoMovimiento(tipoMovimiento);
				movimientos.setCantidad(cantidad);
				movimientos.setFechaHora(java.sql.Timestamp.valueOf(fecha));
				movimientos.setOrigenDestino(origenDestino);
				movimientos.setObservaciones(observaciones);
				movimientos.setIdUsuario(idUsuario);
				list.add(movimientos);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}

	private List<Movimientos> leerCVS(UploadedFile uploadedFile, int idUsuario) {

		List<Movimientos> list = new ArrayList<>();
		Movimientos p;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(uploadedFile.getInputstream()))) {
			String[] fila;
			csvReader.readNext();
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {

					p = new Movimientos();
					p.setIdProducto(Integer.valueOf(fila[0]));
					p.setTipoMovimiento(fila[1]);
					p.setCantidad(Integer.valueOf(fila[2]));
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime fecha = LocalDateTime.parse(fila[3], formatter);
					p.setFechaHora(java.sql.Timestamp.valueOf(fecha));
					p.setOrigenDestino(fila[4]);
					p.setObservaciones(fila[5]);
					p.setIdUsuario(idUsuario);
					list.add(p);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}
}
