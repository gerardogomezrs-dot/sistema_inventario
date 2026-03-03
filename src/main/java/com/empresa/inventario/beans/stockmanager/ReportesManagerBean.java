package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IReporteService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("reportesManagerBean")
@javax.faces.view.ViewScoped
@Data
public class ReportesManagerBean implements Serializable {

	private static final String UNUSED = "unused";

	private static final long serialVersionUID = 1L;

	private Date fechaInicio;

	private Date fechaFin;

	private  List<ReportesMovimiento> listaReporteMovimientos;

	private  List<ReporteInventarioValorizado> listaInventarioValorizado;

	private  List<ReporteStockBajo> listaStockBajo;

	private  List<ReporteAuditoriaUsuario> listaAuditoriaUsuario;

	private  List<ReporteRotacionInventario> listaIndiceInventario;

	private  List<ReporteClasificacionABC> clasificacionABCs;

	private String reporteReabastecimiento = null;

	private String reporteInventarioValorizado = null;

	private String reporteMovimientos = null;

	private String reporteAuditoriaUsuario = null;

	private String reporteRotacionInventario = null;

	private String reporteClasificacionABC = null;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	private IReporteService iReporteService;

	@Inject
	public ReportesManagerBean(IAuditoriaService auditoriaService, IReporteService iReporteService) {
		this.auditoriaService = auditoriaService;
		this.iReporteService = iReporteService;
	}

	@PostConstruct
	public void init() {
		Calendar cal = Calendar.getInstance();
		this.fechaFin = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.fechaInicio = cal.getTime();
		buscar();
		buscarInventarioValorizado();
		buscarStockBajo();
		buscarAuditoriaUsuario();
		buscarIndiceRotacion();
		buscarClasificacion();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String fechaHoy = sdf.format(new Date());
		reporteReabastecimiento = "Reporte Reabastecimiento " + fechaHoy;
		reporteInventarioValorizado = "Reporte InventarioValorizado " + fechaHoy;
		reporteMovimientos = "Reporte Movimientos " + fechaHoy;
		reporteAuditoriaUsuario = "Reporte Auditoria Usuario " + fechaHoy;
		reporteRotacionInventario = "Reporte Rotacion Inventario " + fechaHoy;
		reporteClasificacionABC = "Reporte Clasificacion ABC " + fechaHoy;
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteMovimientos(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Movimientos",
				Mensajes.INFO.toString(), idUsuario);
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteInventarioValorizado(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Inventario Valorizado",
				Mensajes.INFO.toString(), idUsuario);
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteStockBajo(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Stock Bajo",
				Mensajes.INFO.toString(), idUsuario);
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteAuditoriaUsuario(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Auditoria Usuario",
				Mensajes.INFO.toString(), idUsuario);
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteRotacionInventario(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Rotacion Inventario",
				Mensajes.INFO.toString(), idUsuario);
	}

	@SuppressWarnings(UNUSED)
	public void exportarReporteClasificacionABC(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Clasificacion ABC",
				Mensajes.INFO.toString(), idUsuario);
	}

	public void buscarStockBajo() {
		listaStockBajo = iReporteService.reporteStockBajo();
		
	}

	public void buscarInventarioValorizado() {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
		
	}

	public void buscar() {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
		
	}

	public void buscarAuditoriaUsuario() {
		listaAuditoriaUsuario = iReporteService.reporteAuditoriaUsuario();
		
	}

	public void buscarIndiceRotacion() {
		listaIndiceInventario = iReporteService.reporteRotacionInventario();
		
	}

	public void buscarClasificacion() {
		clasificacionABCs = iReporteService.reporteClasificacionABC();
		
	}

	public String irAReporteMovimientos() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Movimientos", "entro a Reporte Movimientos",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}

	public String irAReporteInventarioValorizado() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Inventario Valorizado",
				"entro a Reporte Inventario Valorizado", idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}

	public String irAReporteStockBajo() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Stock Bajo", "entro a Reporte Stock Bajo",
				idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteStockBajo.xhtml?faces-redirect=true";
	}

	public String irAReporteAuditoriaUsuario() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Auditoria Usuario",
				"entro a Reporte Auditoria Usuario", idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteAuditoriaUsuario.xhtml?faces-redirect=true";
	}

	public String irAReportePrincipal() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Menu", "entro a Reporte Menu", idUsuario,
				nombreUsuario);
		return "/pages/stock_manager/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Dasboard", "entro a dashboard", idUsuario, nombreUsuario);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irAAuditoriaUsuarioReporte() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Auditoria Usuario",
				"entro a Reporte Auditoria Usuario", idUsuario, nombreUsuario);
		return "/pages/stock_manager/repotes/reporteAuditoriaUsuario.xhtml?faces-redirect=true";
	}

	public String irARotacionInventario() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Rotacion Inventario",
				"entro a Reporte Rotacion Inventario", idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteRotacionInventario.xhtml?faces-redirect=true";
	}

	public String irAReporteClasificacionABC() {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarNavegacion(auditoriaService, "Reporte Clasificacion ABC",
				"entro a Reporte Clasificacion ABC", idUsuario, nombreUsuario);
		return "/pages/stock_manager/reportes/reporteClasificacionABC.xhtml?faces-redirect=true";
	}
}
