package com.empresa.inventario.service;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.utils.CrearCodigoBarra;
import com.opencsv.CSVReader;

@Named("productoService")
@ApplicationScoped
public class ProductosServiceImp implements IProductoService {

	private ProductosDAO productosDAO = new ProductosDAO();
	
	private MovimientosDAO dao = new MovimientosDAO();

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductosServiceImp.class);

	@Override
	public void delete(int idProducto) {
		try {
			if (idProducto == 0) {
				throw new ExceptionMessage("Sin Datos");
			} else {
				productosDAO.eliminarProducto(idProducto);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			throw new ExceptionMessage("No se puede eliminar el producto");
		}
	}

	@Override
	public List<Productos> create(List<Productos> productosLista) {
		ProductosDAO dao = new ProductosDAO();

		if (productosLista == null || productosLista.isEmpty()) {
			throw new ExceptionMessage("Lista vacia");
		}

		List<Productos> listaProducto = new ArrayList<>();

		for (Productos p : productosLista) {
			try {
				p.setCodigoBarras(CrearCodigoBarra.generarCodigoBarra(p.getCodigoBarras()));

				listaProducto.add(p);
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
		try {
			for (Productos i : listaProducto) {
				dao.guardar(i);
			}

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return productosLista;
	}

	@Override
	public List<Productos> getAll() {
		List<Productos> getProductos = new ArrayList<>();
		productosDAO = new ProductosDAO();
		try {
			getProductos = productosDAO.getAll();

			if (getProductos.isEmpty()) {
				throw new ExceptionMessage("Lista Vacia ");
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return getProductos;
	}

	@Override
	public void update(Productos productos) {
		try {
			if (productos == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				ProductosDAO dao = new ProductosDAO();
				dao.actualizar(productos);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	@Override
	public List<Productos> cargaArchivos(UploadedFile uploadedFile) {
		List<Productos> p = new ArrayList<>();
		if (uploadedFile.getFileName() == null || uploadedFile.getFileName().trim().isEmpty()) {
			throw new ExceptionMessage("Inserta un archivo");
		}
		String fileName = "";
		fileName = uploadedFile.getFileName().toLowerCase();

		if (fileName.endsWith(".csv")) {
			try {
				p = leerCVS(uploadedFile);
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

	private List<Productos> leerExcel(UploadedFile uploadedFile) {
		List<Productos> productosList = new ArrayList<>();
		Productos productos;
		try {
			Workbook workbook = WorkbookFactory.create(uploadedFile.getInputstream());
			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue;
				Cell cellNombre = row.getCell(0);
				Cell cellDescripcion = row.getCell(2);
				Cell cellCodigoBarras = row.getCell(1);
				Cell cellIdCategoria = row.getCell(3);
				Cell cellUnidad = row.getCell(4);
				Cell cellPrecioUnitario = row.getCell(5);
				Cell cellStockActual = row.getCell(6);
				Cell cellStockMinimo = row.getCell(7);
				Cell cellUbicacion = row.getCell(8);
				Cell cellActivo = row.getCell(9);
				Cell cellIdProveedor = row.getCell(10);

				productos = new Productos();

				DataFormatter dataFormatter = new DataFormatter();

				long valorLong = 0;

				if (cellCodigoBarras != null) {
					if (cellCodigoBarras.getCellType() == CellType.NUMERIC) {
						valorLong = (long) cellCodigoBarras.getNumericCellValue();
					} else if (cellCodigoBarras.getCellType() == CellType.STRING) {
						valorLong = Long.parseLong(cellCodigoBarras.getStringCellValue());
					}
				}
				productos.setNombre(dataFormatter.formatCellValue(cellNombre));
				productos.setDescripcion(dataFormatter.formatCellValue(cellDescripcion));
				productos.setCodigoBarras(String.valueOf(valorLong));
				String idCategoria = dataFormatter.formatCellValue(cellIdCategoria);
				int categoria = NumberUtils.toInt(idCategoria, 0);

				productos.setIdCategoria(categoria);
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
				String idProveedor = dataFormatter.formatCellValue(cellIdProveedor);
				int idProveedores = NumberUtils.toInt(idProveedor);
				productos.setIdProveedor(idProveedores);
				productosList.add(productos);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return productosList;
	}

	private List<Productos> leerCVS(UploadedFile uploadedFile) {
		List<Productos> list = new ArrayList<>();
		Productos p;
		try (CSVReader csvReader = new CSVReader(new InputStreamReader(uploadedFile.getInputstream()))) {
			String[] fila;
			csvReader.readNext();
			while ((fila = csvReader.readNext()) != null) {
				if (fila.length >= 2) {
					p = new Productos();
					p.setNombre(fila[0]);
					p.setCodigoBarras(fila[1]);
					p.setDescripcion(fila[2]);
					p.setIdCategoria(Integer.parseInt(fila[3]));
					p.setUnidad(fila[4]);
					p.setPrecioUnitario(Double.parseDouble(fila[5]));
					p.setStockActual(Integer.parseInt(fila[6]));
					p.setStockMinimo(Integer.parseInt(fila[7]));
					p.setUbicacion(fila[8]);
					p.setActivo(Boolean.parseBoolean(fila[9]));
					p.setIdProveedor(Integer.parseInt(fila[10]));

					list.add(p);
				}
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}

	@Override
	public Productos getByCodigoBarras(String codigoBarras) {
		Productos p = new Productos();
		try {
			
				p = productosDAO.getByIdCodigoBarras(codigoBarras);
				if(p.getNombre()==null) {
					throw new ExceptionMessage("Producto no encontrado");
				} 

		} catch (Exception e) {
			e.printStackTrace();
			
			
		}
		return p;
	}

	@Override
	public void bajaProducto(int idProducto) {
		try {
			productosDAO.bajaProducto(idProducto);
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	@Override
	public List<Productos> getStockBajo() {
		List<Productos> productos = new ArrayList<>();
		try {
			productos = productosDAO.getStockBajo();
			return productos.stream().filter(p -> p.getStockActual() <= p.getStockMinimo())
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return productos;
	}

	public int contarCriticos() {
		return getStockBajo().size();
	}

	@Override
	public List<Productos> sinExistencias() {
		List<Productos> list = new ArrayList<>();
		try {
			list = productosDAO.getProductosFaltantes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int totalStockBajo() {
		int  totalStock = 0;
		totalStock = productosDAO.getTotalStockBajo();
		return totalStock;
	}

	@Override
	public int totalMovimientos(int idUsuario) {
		int totalMovimientos = 0;
		totalMovimientos = dao.totalMovimientos(idUsuario);	
		return totalMovimientos;
	}

}
