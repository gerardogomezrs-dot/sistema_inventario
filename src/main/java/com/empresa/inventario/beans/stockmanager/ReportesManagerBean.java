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

import com.empresa.inventario.model.Auditoria;
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

	private static final long serialVersionUID = 1L;

	private Date fechaInicio;

	private Date fechaFin;

	private transient List<ReportesMovimiento> listaReporteMovimientos;

	private transient List<ReporteInventarioValorizado> listaInventarioValorizado;

	private transient List<ReporteStockBajo> listaStockBajo;

	private transient List<ReporteAuditoriaUsuario> listaAuditoriaUsuario;

	private transient List<ReporteRotacionInventario> listaIndiceInventario;

	private transient List<ReporteClasificacionABC> clasificacionABCs;

	private String nombreArchivo_1;

	private String nombreArchivo_2;

	private String nombreArchivo_3;

	private String nombreArchivo_4;

	private String nombreArchivo_5;

	private String nombreArchivo_6;

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
		nombreArchivo_1 = "Reporte Reabastecimiento " + fechaHoy;
		nombreArchivo_2 = "Reporte Inventario Valorizado " + fechaHoy;
		nombreArchivo_3 = "Reporte Movimientos " + fechaHoy;
		nombreArchivo_4 = "Reporte Auditoria Usuario " + fechaHoy;
		nombreArchivo_5 = "Reporte Rotacion Inventario " + fechaHoy;
		nombreArchivo_6 = "Reporte Clasificacion ABC " + fechaHoy;
	}

	@SuppressWarnings("unused")
	public void exportarReporteMovimientos(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	@SuppressWarnings("unused")
	public void exportarReporteInventarioValorizado(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de Inventario Valorizado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	@SuppressWarnings("unused")
	public void exportarReporteStockBajo(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de reabastecimiento");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	@SuppressWarnings("unused")
	public void exportarReporteAuditoriaUsuario(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de auditoria usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	@SuppressWarnings("unused")
	public void exportarReporteRotacionInventario(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de rotacion inventario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	@SuppressWarnings("unused")
	public void exportarReporteClasificacionABC(Object document) {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.EXPORTAR_REPORTE));
		auditoria.setAccion(
				Mensajes.USUARIO + nombreUsuario + " realizo la exportación del reporte de Clasificacion ABC");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
	}

	public void buscarStockBajo()  {
		listaStockBajo = iReporteService.reporteStockBajo();
	}

	public void buscarInventarioValorizado() {
		listaInventarioValorizado = iReporteService.reporteInventarioValorizado();
	}

	public void buscar() {
		listaReporteMovimientos = iReporteService.movimientos(fechaInicio, fechaFin);
	}

	public void buscarAuditoriaUsuario()  {
		listaAuditoriaUsuario = iReporteService.reporteAuditoriaUsuario();
	}

	public void buscarIndiceRotacion() {
		listaIndiceInventario = iReporteService.reporteRotacionInventario();
	}

	public void buscarClasificacion() {
		clasificacionABCs = iReporteService.reporteClasificacionABC();
	}

	public String irAReporteMovimientos() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a Reporte Movimientos");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteMovimientos.xhtml?faces-redirect=true";
	}

	public String irAReporteInventarioValorizado() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a Reporte Inventario Valorizado");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteInventarioValorizado.xhtml?faces-redirect=true";
	}

	public String irAReporteStockBajo() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a Reporte Stock Bajo");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteStockBajo.xhtml?faces-redirect=true";
	}

	public String irAReporteAuditoriaUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego reporte Auditoria Usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteAuditoriaUsuario.xhtml?faces-redirect=true";
	}

	public String irAReportePrincipal() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a Reporte Principal" + "");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reportes.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a Dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
	}

	public String irAAuditoriaUsuarioReporte() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a reporte Auditoria Usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/repotes/reporteAuditoriaUsuario.xhtml?faces-redirect=true";
	}

	public String irARotacionInventario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a reporte Rotación Inventario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteRotacionInventario.xhtml?faces-redirect=true";
	}

	public String irAReporteClasificacionABC() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a reporte Rotación Inventario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/stock_manager/reportes/reporteClasificacionABC.xhtml?faces-redirect=true";
	}
}
