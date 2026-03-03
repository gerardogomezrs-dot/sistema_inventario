package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.io.Serializable;
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

import com.empresa.inventario.dao.MovimientosDAO;
import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Movimientos;
import com.opencsv.CSVReader;

@Named("movimientoService")
@ApplicationScoped
public class MovimientosServiceImpl implements IMovimientosService, Serializable {

	private static final long serialVersionUID = 1L;

	private MovimientosDAO dao;

	private ProductosDAO productosDAO;

	@Override
	public List<Movimientos> save(List<Movimientos> movimientosL) {
		List<Movimientos> list = new ArrayList<>();
		try {
			if (movimientosL == null) {
				throw new ExceptionMessage("Vacio");
			}
			for (Movimientos movimientos : movimientosL) {
				System.err.println("movimientos id" + movimientos.getIdUsuario());

				int totalStock = 0;
				int unidad = movimientos.getCantidad();

				dao = new MovimientosDAO();
				dao.guardar(movimientos);
				productosDAO = new ProductosDAO();
				if (movimientos.getTipoMovimiento().equals("Entrada")) {
					int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
					totalStock = nuevoSock + movimientos.getCantidad();
					productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
				}
				if (movimientos.getTipoMovimiento().equals("Salida")) {
					int nuevoSock = productosDAO.getByIdProducto(movimientos.getIdProducto());
					totalStock = nuevoSock - movimientos.getCantidad();
					productosDAO.actualizarStock(movimientos.getIdProducto(), totalStock);
				}
				if (movimientos.getTipoMovimiento().equals("Ajuste")) {
					productosDAO.actualizarStock(movimientos.getIdProducto(), unidad);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return movimientos;
	}

	@Override
	public List<Movimientos> cargaMasiva(UploadedFile uploadedFile) {
		List<Movimientos> p = new ArrayList<>();
		if (uploadedFile.getFileName() == null || uploadedFile.getFileName().trim().isEmpty()) {
			throw new ExceptionMessage("Inserta un archivo");
		}
		String fileName = "";
		fileName = uploadedFile.getFileName().toLowerCase();

		if (fileName.endsWith(".csv")) {
			try {
				p = leerCVS(uploadedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (fileName.endsWith("xlsx") || fileName.endsWith("lsx")) {
			try {
				p = leerExcel(uploadedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".lsx") && !fileName.endsWith(".csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}

		return p;
	}

	private List<Movimientos> leerExcel(UploadedFile file) {
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
				Cell cellIdUsuario = row.getCell(5);
				Cell cellObservaciones = row.getCell(6);

				DataFormatter dataFormatter = new DataFormatter();

				String idProducto = dataFormatter.formatCellValue(cellIdProducto);
				int id = NumberUtils.toInt(idProducto, 0);
				String tipoMovimiento = cellTipoMovimiento.getStringCellValue();
				String cantidadString = dataFormatter.formatCellValue(cellCantidad);
				int cantidad = NumberUtils.toInt(cantidadString, 0);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime fecha = LocalDateTime.parse(cellFechaHora.getStringCellValue(), formatter);
				String origenDestino = cellOrigenDestino.getStringCellValue();
				String idUsuarioString = dataFormatter.formatCellValue(cellIdUsuario);
				int idUsuario = NumberUtils.toInt(idUsuarioString, 0);
				String observaciones = cellObservaciones.getStringCellValue();
				movimientos = new Movimientos();
				movimientos.setIdProducto(id);
				movimientos.setTipoMovimiento(tipoMovimiento);
				movimientos.setCantidad(cantidad);
				movimientos.setFechaHora(java.sql.Timestamp.valueOf(fecha));
				movimientos.setOrigenDestino(origenDestino);
				movimientos.setIdUsuario(idUsuario);
				movimientos.setObservaciones(observaciones);

				list.add(movimientos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<Movimientos> leerCVS(UploadedFile uploadedFile) {

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
					LocalDateTime fecha = LocalDateTime.parse(fila[3].toString(), formatter);
					p.setFechaHora(java.sql.Timestamp.valueOf(fecha));
					p.setOrigenDestino(fila[4]);
					p.setIdUsuario(Integer.valueOf(fila[5]));
					p.setObservaciones(fila[6]);

					list.add(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
