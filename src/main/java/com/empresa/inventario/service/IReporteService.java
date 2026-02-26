package com.empresa.inventario.service;

import java.util.Date;
import java.util.List;

import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;

public interface IReporteService {
	
	List<ReportesMovimiento> movimientos(Date inicio, Date fin);
	
	List<ReporteInventarioValorizado> reporteInventarioValorizado();
	
	List<ReporteStockBajo> reporteStockBajo();
	
	List<ReporteAuditoriaUsuario> reporteAuditoriaUsuario();
	
	List<ReporteRotacionInventario> reporteRotacionInventario();
	
	List<ReporteClasificacionABC> reporteClasificacionABC();

}
