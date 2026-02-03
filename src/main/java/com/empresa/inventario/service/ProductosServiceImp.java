package com.empresa.inventario.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
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

import com.empresa.inventario.dao.ProductosDAO;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.utils.CrearCodigoBarra;
import com.opencsv.CSVReader;

@Named("productoService")
@ApplicationScoped
public class ProductosServiceImp implements IProductoService {

	private ProductosDAO productosDAO;

	private Productos productos;

	@Override
	public void delete(int idProducto) throws Exception {
		if (idProducto == 0) {
			throw new ExceptionMessage("Sin Datos");
		} else {
			productosDAO.eliminarProducto(idProducto);
		}
	}

	@Override
	public List<Productos> create(List<Productos> productosLista) throws Exception {
		for (Productos productos : productosLista) {
			try {
				if (productos == null) {
					throw new ExceptionMessage("Vacio");
				} else {
					productos.setCodigoBarras(CrearCodigoBarra.generarCodigoBarra(productos.getCodigoBarras()));
					ProductosDAO dao = new ProductosDAO();
					dao.guardar(productos);
				}

			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return productosLista;

	}

	@Override
	public List<Productos> getAll() throws Exception {
		List<Productos> getProductos = new ArrayList<>();
		productosDAO = new ProductosDAO();
		getProductos = productosDAO.getAll();
		try {
			if (getProductos.size() != 0) {
				getProductos = productosDAO.getAll();
			}
			if (getProductos.size() == 0) {
				throw new ExceptionMessage("Vacio");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getProductos;

	}

	@Override
	public void update(Productos productos) throws Exception {
		try {
			if (productos == null) {
				throw new ExceptionMessage("Vacio");
			} else {

				ProductosDAO dao = new ProductosDAO();
				dao.actualizar(productos);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}

	@Override
	public List<Productos> cargaArchivos(UploadedFile uploadedFile) throws Exception {
		List<Productos> productos = new ArrayList<Productos>();

		String fileName = "";
		fileName = uploadedFile.getFileName().toLowerCase();
		System.err.println("fileName " + fileName);

		if (fileName.endsWith(".csv")) {
			try {
				productos = leerCVS(uploadedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (fileName.endsWith("xlsx") || fileName.endsWith("lsx")) {
			try {
				productos = leerExcel(uploadedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return productos;
	}

	private List<Productos> leerExcel(UploadedFile uploadedFile) throws EncryptedDocumentException, IOException {
		List<Productos> productosList = new ArrayList<Productos>();
		Workbook workbook = WorkbookFactory.create(uploadedFile.getInputstream());
		Sheet sheet = workbook.getSheetAt(0); // Primera hoja
		for (Row row : sheet) {
			if (row.getRowNum() == 0)
				continue;

			Cell cellNombre = row.getCell(0);
			Cell cellDescripcion = row.getCell(1);
			Cell cellCodigoBarras = row.getCell(2);
			Cell cellIdCategoria = row.getCell(3);
			Cell cellUnidad = row.getCell(4);
			Cell cellPrecioUnitario = row.getCell(5);
			Cell cellStockActual = row.getCell(6);
			Cell cellStockMinimo = row.getCell(7);
			Cell cellUbicacion = row.getCell(8);
			Cell cellActivo = row.getCell(9);

			productos = new Productos();

			DataFormatter dataFormatter = new DataFormatter();

			productos.setNombre(dataFormatter.formatCellValue(cellNombre));
			productos.setDescripcion(dataFormatter.formatCellValue(cellDescripcion));
			productos.setCodigoBarras(dataFormatter.formatCellValue(cellCodigoBarras));
			String idCategoria = dataFormatter.formatCellValue(cellIdCategoria);
			int stock = NumberUtils.toInt(idCategoria, 0);

			productos.setIdCategoria(stock);
			productos.setUnidad(dataFormatter.formatCellValue(cellUnidad));
			String precioUnitario = dataFormatter.formatCellValue(cellPrecioUnitario);
			double precioU = NumberUtils.toDouble(precioUnitario, 0.0);
			productos.setPrecioUnitario(precioU);
			String stockActual = dataFormatter.formatCellValue(cellStockActual);
			String stockMinimo = dataFormatter.formatCellValue(cellStockMinimo);
			int stockAct = NumberUtils.toInt(stockActual, 0);
			int stockMin = NumberUtils.toInt(stockMinimo, 0);
			productos.setStockActual(stockAct);
			productos.setStockMinimo(stockMin);
			productos.setUbicacion(dataFormatter.formatCellValue(cellUbicacion));
			productos.setActivo(Boolean.parseBoolean(dataFormatter.formatCellValue(cellActivo)));

			productosList.add(productos);
		}
		return productosList;
	}

	private List<Productos> leerCVS(UploadedFile uploadedFile) {
		List<Productos> productos = new ArrayList<Productos>();
		Productos p;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(uploadedFile.getInputstream()))) {
			String[] fila;
			csvReader.readNext();
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {

					p = new Productos();
					p.setCodigoBarras(fila[1]);
					p.setNombre(fila[0]);
					p.setDescripcion(fila[2]);
					p.setIdCategoria(Integer.parseInt(fila[3]));
					p.setUnidad(fila[4]);
					p.setPrecioUnitario(Double.parseDouble(fila[5]));
					p.setStockActual(Integer.parseInt(fila[6]));
					p.setStockMinimo(Integer.parseInt(fila[7]));
					p.setUbicacion(fila[8]);
					p.setActivo(Boolean.parseBoolean(fila[9]));
					productos.add(p);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return productos;
	}

}
