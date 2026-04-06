package com.empresa.inventario.beans.almacen;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.model.ReporteBajaRotacion;
import com.empresa.inventario.model.ReporteKardex;
import com.empresa.inventario.model.ReporteStockBajoAlmacen;
import com.empresa.inventario.model.ReportesExistencias;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IReporteAlmacenService;
import com.empresa.inventario.utils.Mensajes;
import com.empresa.inventario.utils.ReportesUtils;

import lombok.Data;

@Named("inventarioReporteAlmacenBean")
@ViewScoped
@Data
public class ReportesAlmacenBean implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private transient IAuditoriaService auditoriaService;

	private String reporteExistencias;

	private String reporteBajaRotacion;

	private String reporteStockCritico;

	private String reporteKardex;

	private transient List<ReportesExistencias> reportesExistencias;

	private transient List<ReporteBajaRotacion> reporteBajaRotacions;

	private transient List<ReporteStockBajoAlmacen> reporteStockBajos;

	private transient List<ReporteKardex> reporteKardexs;

	private IReporteAlmacenService almacenService;

	private int idUsuario;

	private String nombreUsuario;

	@Inject
	public ReportesAlmacenBean(IAuditoriaService auditoriaService, IReporteAlmacenService almacenService) {
		this.auditoriaService = auditoriaService;
		this.almacenService = almacenService;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String fechaHoy = sdf.format(new Date());
		reporteExistencias = "Reporte Existencias " + fechaHoy;
		reporteStockCritico = "Reporte Stock Critico " + fechaHoy;
		reporteBajaRotacion = "Reporte Baja Rotacion " + fechaHoy;
		reporteKardex = "Reporte Kartex " + fechaHoy;
	}

	@PostConstruct
	public void init() {
		buscarReporteExistencias();
		buscarReporteRotacionBaja();
		buscarReporteStockBajo();
		buscarReporteKardex();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();
		nombreUsuario = user.getNombre();
	}

	private void buscarReporteKardex() {
		reporteKardexs = almacenService.reporteKardexs();
	}

	private void buscarReporteStockBajo() {
		reporteStockBajos = almacenService.reporteStockBajoAlmacens();
	}

	private void buscarReporteRotacionBaja() {
		reporteBajaRotacions = almacenService.reportesBajaRotacions();
	}

	public void buscarReporteExistencias() {
		reportesExistencias = almacenService.getReporteExistencias();
	}

	public String irAReporteExistencias() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Reporte Existencias", "entro a Reporte Existencias", idUsuario,
				nombreUsuario);
		return "/pages/almacen/reportes/reporteExistencias.xhtml?faces-redirect=true";
	}

	public String irAReporteBajaRotacion() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Reporte Existencias", "entro a Baja Rotacion", idUsuario,
				nombreUsuario);
		return "/pages/almacen/reportes/reporteBajaRotacion.xhtml?faces-redirect=true";
	}

	public String irAReporteMovimientos() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Reporte Baja Rotacion", "entro a Reporte Kartex", idUsuario,
				nombreUsuario);
		return "/pages/almacen/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}

	public String irAReporteStockCritico() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Reporte Stock Critico", "entro a Reporte Stock Critico",
				idUsuario, nombreUsuario);
		return "/pages/almacen/reportes/reporteStockCritico.xhtml?faces-redirect=true";
	}

	public String irADashBoard() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);
		return "/pages/almacen/reportes/reportesDashboard.xhtml?faces-redirect=true";
	}

	public String irAMenuPrincipal() {
		BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, "Menu Principal", "entro a Menu Principal", idUsuario,
				nombreUsuario);
		return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
	}

	public void exportarReporteExistenciasExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportacion del Reporte de Existencias",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteExistenciasPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Existencias", Mensajes.INFO.toString(),
				idUsuario);
		ReportesUtils.postProcessPDF(document, reporteExistencias);
	}

	public void exportarReporteBajaRotacionExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de Baja Rotacion",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteBajaRotacionPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de Baja Rotacion",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteBajaRotacion);
	}

	public void exportarReporteStockBajoExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de Stock Bajo",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteStockBajoPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de Stock Bajo",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteStockCritico);
	}

	public void exportarReporteKardexExcel(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de  Kardex",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.exportarReporteExcel(document);
	}

	public void exportarReporteKardexPdf(Object document) {
		BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();
		auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.EXPORTAR_REPORTE.getTexto(),
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación de Reporte de Kardex",
				Mensajes.INFO.toString(), idUsuario);
		ReportesUtils.postProcessPDF(document, reporteKardex);
	}

}
