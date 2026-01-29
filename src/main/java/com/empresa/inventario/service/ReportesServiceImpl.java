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
import com.empresa.inventario.model.ReportesMovimiento;

@Named("reportesService")
@ApplicationScoped
public class ReportesServiceImpl implements IReporteService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ReportesDAO dao = new ReportesDAO();

	@Override
	public List<ReportesMovimiento> movimientos(Date Inicio, Date Fin) throws Exception {
		
		List<ReportesMovimiento> movimientos = new ArrayList<ReportesMovimiento>();
		
		LocalDateTime localDateTimeInicio = Inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime localDateTimeFin = Inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		movimientos = dao.getAllCategorias(localDateTimeInicio, localDateTimeFin);
		
		return movimientos;
	}
	

}
