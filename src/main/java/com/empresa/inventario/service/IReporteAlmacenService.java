package com.empresa.inventario.service;

import java.util.List;

import com.empresa.inventario.model.ReporteBajaRotacion;
import com.empresa.inventario.model.ReporteKardex;
import com.empresa.inventario.model.ReporteStockBajoAlmacen;
import com.empresa.inventario.model.ReportesExistencias;

public interface IReporteAlmacenService {
	
	List<ReportesExistencias> getReporteExistencias();
	
	List<ReporteBajaRotacion> reportesBajaRotacions(); 
	
	List<ReporteStockBajoAlmacen> reporteStockBajoAlmacens();
	
	List<ReporteKardex> reporteKardexs();

}
