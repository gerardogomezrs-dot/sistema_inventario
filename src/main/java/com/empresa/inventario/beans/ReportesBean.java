package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.service.IReporteService;

import lombok.Data;

@Named("inventarioReporteBean")
@javax.faces.view.ViewScoped
@Data
public class ReportesBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IReporteService iReporteService;
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	private List<ReportesMovimiento> listaReporteMovimientos;
	
	private List<ReportesMovimiento> listaInventarioValorizado;
	
	public ReportesBean(){
		
	}
	
	
	@PostConstruct
	public void init() throws Exception {
	
		// Por defecto, últimos 30 días
        Calendar cal = Calendar.getInstance();
        this.fechaFin = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        this.fechaInicio = cal.getTime();
        buscar();
        buscarInventarioValorizado();
	}
	
	
	public void buscar() throws Exception {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
	}
	
	public void buscarInventarioValorizado() throws Exception {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
	}
	
	
	public String irAReporteMovimientos() {
		return "/pages/admin/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}
	
	public String irAReporteInventarioValorizado() {
		return "/pages/admin/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}
	
}
