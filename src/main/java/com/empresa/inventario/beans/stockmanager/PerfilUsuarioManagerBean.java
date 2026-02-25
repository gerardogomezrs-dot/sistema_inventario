package com.empresa.inventario.beans.stockmanager;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IUsuariosService;

import lombok.Data;

@Named("perfilUsuarioManagerBean")
@javax.faces.view.ViewScoped
@Data
public class PerfilUsuarioManagerBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	private IUsuariosService iUsuariosService;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public PerfilUsuarioManagerBean(IUsuariosService iUsuariosService, IAuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
		this.iUsuariosService = iUsuariosService;
	}

	@PostConstruct
	public void init() {
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		
			int id = user.getIdUsuario();
			usuario = iUsuariosService.getByIdUsuario(id);

			idUsuario = user.getIdUsuario();
			nombreUsuario = user.getNombre();
		
	}

	public void actualizarPerfil() {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			}
			iUsuariosService.updateProfile(usuario);
			iUsuariosService.updateProfile(usuario);
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizar");
			auditoria.setAccion("El usuario " + nombreUsuario + " realizo una actualización");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.printStackTrace();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Error");
			auditoria.setAccion("Error: " + e.getMessage());
			auditoria.setNivel("WARN");
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public String irAIndex() {
		return "/pages/stock_manager/dashboard?faces-redirect=true";
	}
}
