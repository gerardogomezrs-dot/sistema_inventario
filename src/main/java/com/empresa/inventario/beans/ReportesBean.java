package com.empresa.inventario.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
	
	private List<ReportesMovimiento> listaStockMinimo;
	
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
	
	
	public String getNombreArchivoInventario() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        String fechaHoy = sdf.format(new Date());
        return "inventario_valorizado_" + fechaHoy;
    }
	
	public void buscar() throws Exception {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
	}
	
	public void buscarInventarioValorizado() throws Exception {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
	}
	
	
	public void buscarStockBajo() throws Exception {
		listaStockMinimo = iReporteService.reporteStockBajo();
	}
	
	public String irAReporteMovimientos() {
		return "/pages/admin/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}
	
	public String irAReporteInventarioValorizado() {
		return "/pages/admin/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}
	
	
	public String irAReporteStockBajo() {
		return "/pages/admin/reportes/reporteStockBajo.xhtml?faces-redirect=true";
	}
	
}
