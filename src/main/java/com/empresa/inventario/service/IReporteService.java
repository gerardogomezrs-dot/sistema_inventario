package com.empresa.inventario.service;

import java.util.Date;
import java.util.List;

import com.empresa.inventario.model.ReportesMovimiento;

public interface IReporteService {
	
	List<ReportesMovimiento> movimientos(Date Inicio, Date Fin) throws Exception;

}
