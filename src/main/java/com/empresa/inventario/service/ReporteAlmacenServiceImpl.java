package com.empresa.inventario.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.dao.ReportesAlmacenDAO;
import com.empresa.inventario.model.ReporteBajaRotacion;
import com.empresa.inventario.model.ReporteKardex;
import com.empresa.inventario.model.ReporteStockBajoAlmacen;
import com.empresa.inventario.model.ReportesExistencias;

@Named("reportesAlmacen	Service")
@ApplicationScoped
public class ReporteAlmacenServiceImpl implements IReporteAlmacenService {

	private ReportesAlmacenDAO almacenDAO;
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReporteAlmacenServiceImpl.class);

	@Override
	public List<ReportesExistencias> getReporteExistencias() {
		List<ReportesExistencias> existencias = new ArrayList<>();
		almacenDAO = new ReportesAlmacenDAO();
		try {
			existencias = almacenDAO.getInventarioValorizado();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return existencias;
	}

	@Override
	public List<ReporteBajaRotacion> reportesBajaRotacions() {
		List<ReporteBajaRotacion> bajaRotacions = new ArrayList<>();
		almacenDAO = new ReportesAlmacenDAO();
		try {
			bajaRotacions = almacenDAO.getBajaRotacion();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return bajaRotacions;
	}

	@Override
	public List<ReporteStockBajoAlmacen> reporteStockBajoAlmacens() {
		List<ReporteStockBajoAlmacen> stockBajoAlmacens = new ArrayList<>();
		almacenDAO = new ReportesAlmacenDAO();
		try {
			stockBajoAlmacens = almacenDAO.getStockCritico();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return stockBajoAlmacens;
	}

	@Override
	public List<ReporteKardex> reporteKardexs() {
		List<ReporteKardex> kardexs = new ArrayList<>();
		almacenDAO = new ReportesAlmacenDAO();
		try {
			kardexs = almacenDAO.getReporteKardex();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return kardexs;
	}
}
