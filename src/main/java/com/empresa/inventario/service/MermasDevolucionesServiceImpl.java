package com.empresa.inventario.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.model.UploadedFile;

import com.empresa.inventario.dao.MermasDevolucionesDAO;
import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.MermasDevoluciones;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.opencsv.CSVReader;

@Named("mermasDevolucionesService")
@ApplicationScoped
public class MermasDevolucionesServiceImpl implements IMermasDevolucionesService {


	@Override
	public List<MermasDevoluciones> getListaMermasDevoluciones() {
		List<MermasDevoluciones> devoluciones = new ArrayList<>();
		MermasDevolucionesDAO devolucionesDAO = new MermasDevolucionesDAO();
		try {
			devoluciones = devolucionesDAO.getAllMermasDevoluciones();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devoluciones;
	}

	@Override
	public void guardarMermasDevoluciones(List<MermasDevoluciones> devoluciones, Usuario usuario) {
		MermasDevolucionesDAO devolucionesDAO = new MermasDevolucionesDAO();
		List<MermasDevoluciones> mermasDevoluciones = new ArrayList<>(devoluciones);
		ProductosDAO dao = new ProductosDAO();
		Productos productos = new Productos();
		try {
			for (MermasDevoluciones mermasDevoluciones2 : mermasDevoluciones) {				
				mermasDevoluciones2.setIdUsuario(usuario.getIdUsuario());
				if (mermasDevoluciones2.getTipo().equals("merma")) {
					productos = dao.getByIdProductoInfo(mermasDevoluciones2.getIdProducto());
					double costo = productos.getPrecioUnitario() * mermasDevoluciones2.getCantidad();
					mermasDevoluciones2.setCostoPerdido(costo);
					devolucionesDAO.guardar(mermasDevoluciones2);
					int stockActual = productos.getStockActual() - mermasDevoluciones2.getCantidad();
					dao.actualizarStock(mermasDevoluciones2.getIdProducto(), stockActual);
				}
				 if (mermasDevoluciones2.getTipo().equals("devolucion")) {
					productos = dao.getByIdProductoInfo(mermasDevoluciones2.getIdProducto());
					double costo = 0;
					mermasDevoluciones2.setCostoPerdido(costo);
					devolucionesDAO.guardar(mermasDevoluciones2);
					int stockActual = productos.getStockActual() + mermasDevoluciones2.getCantidad();
					dao.actualizarStock(mermasDevoluciones2.getIdProducto(), stockActual);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MermasDevoluciones> cargarArchivo(UploadedFile file) {
		List<MermasDevoluciones> devoluciones = new ArrayList<>();
		if (file.getFileName() == null || file.getFileName().trim().isEmpty()) {
			throw new ExceptionMessage("Inserta un archivo");
		}
		String fileName = "";
		fileName = file.getFileName().toLowerCase();
		if (fileName.endsWith(".csv")) {
			try {
				devoluciones = leerCSV(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (fileName.endsWith(".xlsx") || fileName.endsWith(".lsx")) {
			try {
				devoluciones = leerExcel(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".lsx") && !fileName.endsWith(".csv")) {
			throw new ExceptionMessage("Formato no soportado");
		}

		return devoluciones;
	}

	private List<MermasDevoluciones> leerExcel(UploadedFile file) {
		List<MermasDevoluciones> mermasDevoluciones = new ArrayList<>();
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(file.getInputstream());
			Sheet sheet = workbook.getSheetAt(0);
			MermasDevoluciones cat;
			for (Row row : sheet) {
				cat = new MermasDevoluciones();
				if (row.getRowNum() == 0)
					continue;
				Cell cellIdProducto = row.getCell(0);
				Cell cellCantidad = row.getCell(1);
				Cell cellMotivo = row.getCell(2);
				Cell cellTipo = row.getCell(3);

				DataFormatter dataFormatter = new DataFormatter();
				
				
				String idProducto = dataFormatter.formatCellValue(cellIdProducto);
				String cantidad = dataFormatter.formatCellValue(cellCantidad);
				
				int id = NumberUtils.toInt(idProducto);
				int cantidadProductos = NumberUtils.toInt(cantidad);
				String motivo = cellMotivo.getStringCellValue();
				String tipo = cellTipo.getStringCellValue();
				cat.setIdProducto(id);
				cat.setCantidad(cantidadProductos);
				cat.setMotivo(motivo);
				cat.setTipo(tipo);
				mermasDevoluciones.add(cat);
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mermasDevoluciones;
	}

	private List<MermasDevoluciones> leerCSV(UploadedFile file) {
		List<MermasDevoluciones> list = new ArrayList<>();
		MermasDevoluciones p;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputstream()))) {
			String[] fila;
			csvReader.readNext();
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {

					p = new MermasDevoluciones();
					p.setIdProducto(Integer.valueOf(fila[0]));
					p.setCantidad(Integer.valueOf(fila[1]));
					p.setMotivo(fila[2]);
					p.setTipo(fila[3]);
					list.add(p);
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return list;
	}
}
