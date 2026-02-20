package com.empresa.inventario.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.empresa.inventario.dao.ReportesDAO;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;

@Named("reportesService")
@ApplicationScoped
public class ReportesServiceImpl implements IReporteService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ReportesDAO dao = new ReportesDAO();

	@Override
	public List<ReportesMovimiento> movimientos(Date Inicio, Date Fin) throws Exception {
		List<ReportesMovimiento> movimientos = new ArrayList<ReportesMovimiento>();
		LocalDateTime localDateTimeInicio = Inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime localDateTimeFin = Fin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		try {
			movimientos = dao.getAllCategorias(localDateTimeInicio, localDateTimeFin);

			System.err.println("Fechas: "+ localDateTimeInicio+ " " + "" + localDateTimeFin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movimientos;
	}

	@Override
	public List<ReporteInventarioValorizado> reporteInventarioValorizado() throws Exception {
		List<ReporteInventarioValorizado> reportesMovimientos = new ArrayList<ReporteInventarioValorizado>();
		reportesMovimientos = dao.getInventarioValorizado();
		return reportesMovimientos;
	}

	@Override
	public List<ReporteStockBajo> reporteStockBajo() throws Exception {
		List<ReporteStockBajo> list = new ArrayList<ReporteStockBajo>();
		list = dao.getStockBajo();
		return list;
	}

	@Override
	public List<ReporteAuditoriaUsuario> reporteAuditoriaUsuario() throws Exception {
		List<ReporteAuditoriaUsuario> list = new ArrayList<ReporteAuditoriaUsuario>();
		list = dao.getAuditoriaUsuario();
		for(ReporteAuditoriaUsuario movimiento: list) {
			System.err.println(movimiento.getUsuarioResponsable());
		}
		return list;
	}

	@Override
	public List<ReporteRotacionInventario> reporteRotacionInventario() throws Exception {
		List<ReporteRotacionInventario> list = new ArrayList<ReporteRotacionInventario>();
		list = dao.getRotacionInventario();
		return list;
	}

	@Override
	public List<ReporteClasificacionABC> reporteClasificacionABC() throws Exception {
		List<ReporteClasificacionABC> clasificacionABCs = new ArrayList<ReporteClasificacionABC>();
		clasificacionABCs = dao.getClasificacionABC();
		System.err.println("Tamaño lista + " + clasificacionABCs.size());
		return clasificacionABCs;
	}
}
