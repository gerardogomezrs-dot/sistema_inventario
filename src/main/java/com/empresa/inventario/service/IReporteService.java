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
	
	List<ReportesMovimiento> movimientos(Date Inicio, Date Fin) throws Exception;
	
	List<ReporteInventarioValorizado> reporteInventarioValorizado() throws Exception;
	
	List<ReporteStockBajo> reporteStockBajo() throws Exception;
	
	List<ReporteAuditoriaUsuario> reporteAuditoriaUsuario() throws Exception;
	
	List<ReporteRotacionInventario> reporteRotacionInventario() throws Exception;
	
	List<ReporteClasificacionABC> reporteClasificacionABC() throws Exception;

}
