package com.empresa.inventario.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.admin.AuditoriaBean;
import com.empresa.inventario.dao.ReportesDAO;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteMermasDevolucion;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;

@Named("reportesService")
@ApplicationScoped
public class ReportesServiceImpl implements IReporteService {
	private ReportesDAO dao = new ReportesDAO();
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuditoriaBean.class);

	@Override
	public List<ReportesMovimiento> movimientos(Date inicio, Date fin) {
		List<ReportesMovimiento> movimientos = new ArrayList<>();
		LocalDateTime localDateTimeInicio = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime localDateTimeFin = fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		try {
			movimientos = dao.getAllCategorias(localDateTimeInicio, localDateTimeFin);
		} catch (Exception e) {
			e.getMessage();
		}
		return movimientos;
	}

	@Override
	public List<ReporteInventarioValorizado> reporteInventarioValorizado() {
		List<ReporteInventarioValorizado> reportesMovimientos = new ArrayList<>();
		try {
			reportesMovimientos = dao.getInventarioValorizado();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return reportesMovimientos;
	}

	@Override
	public List<ReporteStockBajo> reporteStockBajo() {
		List<ReporteStockBajo> list = new ArrayList<>();
		try {
			list = dao.getStockBajo();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}

	@Override
	public List<ReporteAuditoriaUsuario> reporteAuditoriaUsuario() {
		List<ReporteAuditoriaUsuario> list = new ArrayList<>();
		try {
			list = dao.getAuditoriaUsuario();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}

	@Override
	public List<ReporteRotacionInventario> reporteRotacionInventario() {
		List<ReporteRotacionInventario> list = new ArrayList<>();
		try {
			list = dao.getRotacionInventario();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return list;
	}

	@Override
	public List<ReporteClasificacionABC> reporteClasificacionABC() {
		List<ReporteClasificacionABC> clasificacionABCs = new ArrayList<>();
		try {
			clasificacionABCs = dao.getClasificacionABC();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return clasificacionABCs;
	}

	@Override
	public List<ReporteMermasDevolucion> mermasDevolucions() {
		List<ReporteMermasDevolucion> mermasDevolucions = new ArrayList<>();
		try {
			mermasDevolucions = dao.getReporteMermasDevoluciones();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return mermasDevolucions;
	}
}
