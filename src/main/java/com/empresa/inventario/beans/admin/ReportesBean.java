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

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.ReporteAuditoriaUsuario;
import com.empresa.inventario.model.ReporteClasificacionABC;
import com.empresa.inventario.model.ReporteInventarioValorizado;
import com.empresa.inventario.model.ReporteMermasDevolucion;
import com.empresa.inventario.model.ReporteRotacionInventario;
import com.empresa.inventario.model.ReporteStockBajo;
import com.empresa.inventario.model.ReportesMovimiento;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IReporteService;
import com.empresa.inventario.utils.Mensajes;
import com.empresa.inventario.utils.ReportesUtils;

import lombok.Data;

@Named("inventarioReporteBean")
@javax.faces.view.ViewScoped
@Data
public class ReportesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fechaInicio;

	private Date fechaFin;

	private transient List<ReportesMovimiento> listaReporteMovimientos;

	private transient List<ReporteInventarioValorizado> listaInventarioValorizado;

	private transient List<ReporteStockBajo> listaStockBajo;

	private transient List<ReporteAuditoriaUsuario> listaAuditoriaUsuario;

	private transient List<ReporteRotacionInventario> listaIndiceInventario;

	private transient List<ReporteClasificacionABC> clasificacionABCs;

	private transient List<ReporteMermasDevolucion> mermasDevolucions;

	private String reporteReabastecimiento;

	private String reporteInventarioValorizado;

	private String reporteMovimientos;

	private String reporteAuditoriaUsuario;

	private String reporteRotacionInventario;

	private String reporteClasificacionABC;

	private String reporteMermasDevoluciones;

	private int idUsuario;

	private String nombreUsuario;

	private transient IAuditoriaService auditoriaService;

	private transient IReporteService iReporteService;

	@Inject
	public ReportesBean(IAuditoriaService auditoriaService, IReporteService iReporteService) {
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
		buscarClasficacionABC();
		buscarMermasDevoluciones();
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
		reporteMermasDevoluciones = "Reporte Mermas Devoluciones " + fechaHoy;

	}

	public void exportarReporteStockBajoExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Stock Bajo",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteStockBajoPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Stock Bajo",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteReabastecimiento);
	}

	public void exportarReporteInventarioValorizadoExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Inventario Valorizado",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);

	}

	public void exportarReporteInventarioValorizadoPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Inventario Valorizado",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteInventarioValorizado);

	}

	public void exportarReporteMovimientosExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Movimientos",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteMovimientosPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Movimientos",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteMovimientos);
	}

	public void exportarReporteAuditoriaUsuarioExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Auditoria Usuario",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteAuditoriaUsuarioPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Auditoria Usuario",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteAuditoriaUsuario);
	}

	public void exportarRotacionInventarioExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Rotacion Inventario",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarRotacionInventarioPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Rotacion Inventario",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteRotacionInventario);
	}

	public void exportarClasificacionABCExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Clasificacion ABC",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarClasificacionABCPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Clasificacion ABC",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteClasificacionABC);
	}

	public void exportarMermasDevolucionesExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Mermas Devoluciones",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarMermasDevolucionesPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte Mermas Devoluciones",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteMermasDevoluciones);
	}

	public void buscar() {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
	}

	public void buscarInventarioValorizado() {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
	}

	public void buscarStockBajo() {
		listaStockBajo = iReporteService.reporteStockBajo();
	}

	public void buscarAuditoriaUsuario() {
		listaAuditoriaUsuario = iReporteService.reporteAuditoriaUsuario();
	}

	public void buscarIndiceRotacion() {
		listaIndiceInventario = iReporteService.reporteRotacionInventario();
	}

	public void buscarClasficacionABC() {
		clasificacionABCs = iReporteService.reporteClasificacionABC();
	}

	public void buscarMermasDevoluciones() {
		mermasDevolucions = iReporteService.mermasDevolucions();
	}

	public String irAReporteMovimientos() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Movimiento", "entro a Reporte Movimiento",
				idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}

	public String irAReporteInventarioValorizado() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Inventario Valorizado",
				"entro a Reporte Inventario Valorizado", idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}

	public String irAReporteStockBajo() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Stock Bajo", "entro a Reporte Stock Bajo",
				idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteStockBajo.xhtml?faces-redirect=true";
	}

	public String irAReporteAuditoriaUsuario() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Auditoria Usuario",
				"entro a Reporte Auditoria Usuario", idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteAuditoriaUsuario.xhtml?faces-redirect=true";
	}

	public String irAReporteRotacionInventario() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Rotacion Inventario",
				"entro a Reporte Rotacion Inventario", idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteRotacionInventario.xhtml?faces-redirect=true";
	}

	public String irAReporteMermasDevoluciones() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Mermas Devolucion",
				"entro a Reporte Rotacion Inventario", idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteMermasDevoluciones.xhtml?faces-redirect=true";
	}

	public String irAReportePrincipal() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Principal", "entro a Reporte Principal",
				idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario,
				nombreUsuario);
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irAReporteClasificacionABC() {
		BaseAuditoriaBean baseAuditoriaBean = new BaseAuditoriaBean();
		baseAuditoriaBean.registrarNavegacion(auditoriaService, "Reporte Clasificacion ABC",
				"entro a Reporte Clasificacion ABC", idUsuario, nombreUsuario);
		return "/pages/admin/reportes/reporteClasificacion.xhtml?faces-redirect=true";
	}
}