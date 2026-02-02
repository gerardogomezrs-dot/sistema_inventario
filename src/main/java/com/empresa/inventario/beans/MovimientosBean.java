package com.empresa.inventario.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.empresa.inventario.model.Movimientos;
import com.empresa.inventario.model.Productos;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IMovimientosService;
import com.empresa.inventario.service.IProductoService;

import lombok.Data;

@Named("movimientosBean")
@javax.faces.view.ViewScoped
@Data
public class MovimientosBean implements Serializable {

	/**
	 * 
	 */

	private static final Logger logger = LoggerFactory.getLogger(MovimientosBean.class);

	private static final long serialVersionUID = 1L;

	private List<Movimientos> list;

	private List<Movimientos> filteredList; // Lista para almacenar los resultados filtrados

	private Movimientos movimientos;

	private List<Productos> listProductos;

	@Inject
	private IMovimientosService service;

	@Inject
	private IProductoService iProductoService;

	private List<Movimientos> listaMovimientosGuardar = new ArrayList<Movimientos>();

	public MovimientosBean() {

	}

	@PostConstruct
	public void init() {
		try {
			this.movimientos = new Movimientos(); // Inicializamos el objeto
			listaMovimientos();
			listProductos = iProductoService.getAll();

			// 1. Recuperamos el objeto "usuario" completo que guardaste en el login
			// Asegúrate de importar tu clase Usuario/Model correspondiente
			Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
					.get("sessionUsuario");

			// 2. Si el usuario existe en sesión, le sacamos el ID
			if (user != null) {
				this.movimientos.setIdUsuario(user.getIdUsuario()); // O el nombre de tu getter del ID
				logger.info("LOG: Usuario recuperado de sesión: " + user.getNombre());

			} else {
				logger.info("LOG: No hay ninguna sesión activa con 'sessionUsuario'");

			}

		} catch (Exception e) {
			logger.error("Error en init de MovimientosBean: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<Movimientos> listaMovimientos() throws Exception {
		return list = service.getAll();
	}

	public void save() throws Exception {
		listaMovimientosGuardar.add(movimientos);
		
		this.movimientos = new Movimientos();
	}

	public void saveTable() throws Exception {
		
		System.out.println("Guardar");
		
		if(listaMovimientosGuardar != null && !listaMovimientosGuardar.isEmpty()) {
		service.save(listaMovimientosGuardar);
		}
		if(listaMovimientosGuardar != null) {
			listaMovimientosGuardar.clear();
		}
	}

	public String irANuevoMovimiento() {
		return "/pages/admin/movimientos/movimientos.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		return "/pages/admin/dashboard.xhtml?faces-redirect=true";
	}

	public String irATablaMovimientos() {
		return "/pages/admin/movimientos/tablaMovimientos.xhtml?faces-redirect=true";
	}

}
