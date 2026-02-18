package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IReporteService;

import lombok.Data;

@Named("inventarioReporteBean")
@javax.faces.view.ViewScoped
@Data
public class ReportesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IReporteService iReporteService;

	private Date fechaInicio;

	private Date fechaFin;

	private List<ReportesMovimiento> listaReporteMovimientos;

	private List<ReportesMovimiento> listaInventarioValorizado;

	private List<ReportesMovimiento> listaStockBajo;

	private String nombreArchivo_1;

	private String nombreArchivo_2;

	private String nombreArchivo_3;
	
	private int idUsuario;

	private String nombreUsuario;
	
	@Inject
	private IAuditoriaService auditoriaService;

	public ReportesBean() {
	}

	@PostConstruct
	public void init() throws Exception {
		Calendar cal = Calendar.getInstance();
		this.fechaFin = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.fechaInicio = cal.getTime();
		buscar();
		buscarInventarioValorizado();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
		System.err.println("IdUsuario "+ idUsuario);
		exportarReporteInventarioValorizado();
		exportarReporteMovimientos();
		exportarReporteReabastecimiento();
	}

	public void exportarReporteReabastecimiento() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String fechaHoy = sdf.format(new Date());
		nombreArchivo_1 = "Reporte Reabastecimiento " + fechaHoy;
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Exportar Reporte");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo la exportación del reporte de reabastecimiento");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void exportarReporteInventarioValorizado() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String fechaHoy = sdf.format(new Date());
		nombreArchivo_2 = "Reporte Inventario Valorizado " + fechaHoy;
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Exportar Reporte");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo la exportación del reporte de Inventario Valorizado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void exportarReporteMovimientos() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String fechaHoy = sdf.format(new Date());
		nombreArchivo_3 = "Reporte Movimientos " + fechaHoy;
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Exportar Reporte");
		auditoria.setAccion("El usuario " + nombreUsuario + " realizo la exportación del reporte de movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void buscar() throws Exception {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
	}

	public void buscarInventarioValorizado() throws Exception {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
	}

	public void buscarStockBajo() throws Exception {
		listaStockBajo = iReporteService.reporteStockBajo();
	}

	public String irAReporteMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegp");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a Reporte Movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}

	public String irAReporteInventarioValorizado() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegp");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a Reporte Inventario Valorizado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}

	public String irAReporteStockBajo() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo("Navegp");
		auditoria.setAccion("El usuario " + nombreUsuario + " navego a Reporte Stock Bajo"
				+ "");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/reportes/reporteStockBajo.xhtml?faces-redirect=true";
	}

	public String irAReportePrincipal() {
		return "/pages/admin/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}
}
