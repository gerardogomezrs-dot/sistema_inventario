package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Auditoria;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IUsuariosService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("usuariosBean")
@javax.faces.view.ViewScoped
@Data
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private IUsuariosService iUsuariosService;

	private List<Usuario> list;

	private Usuario usuario;

	private int idUsuario;

	private String nombreUsuario;

	private IAuditoriaService auditoriaService;

	@Inject
	public UsuariosBean(IAuditoriaService auditoriaService, IUsuariosService iUsuariosService) {
		this.auditoriaService = auditoriaService;
		this.iUsuariosService = iUsuariosService;
	}

	@PostConstruct
	public void init() {

		listaUsuarios();
		Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("sessionUsuario");
		idUsuario = user.getIdUsuario();

	}

	public String irANuevoUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego nuevo usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String irATablaUsuario() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a tabla usuario");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/usuarios/tablaUsuarios.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		Auditoria auditoria = new Auditoria();
		auditoria.setFechaAuditoria(new Date());
		auditoria.setIdUsuario(idUsuario);
		auditoria.setClaseOrigen(this.getClass().getName());
		auditoria.setMetodo(String.valueOf(Mensajes.NAVEGACION));
		auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " navego a dashboard");
		auditoria.setNivel("INFO");
		auditoriaService.registroAuditoria(auditoria);
		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void guardar() {
		try {
			iUsuariosService.save(usuario);
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Guardar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo un guardo un nuevo registro");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);

		} catch (ExceptionMessage e) {

			mensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(Mensajes.ERROR.toString());
			auditoria.setAccion(Mensajes.ERROR + e.getMessage());
			auditoria.setNivel(Mensajes.ERROR.toString());
			auditoriaService.registroAuditoria(auditoria);
		}
	}
	
	public void eliminar() {
		try {
			iUsuariosService.delete(usuario.getIdUsuario(), idUsuario);
			list = iUsuariosService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario eliminado", "El usuario fue eliminado correctamente"));
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Eliminar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " elimino un registro");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);

		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}
	}

	public void actualizar() {
		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				iUsuariosService.update(usuario);
				list = iUsuariosService.getAll();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Usuario actualizado", "El usuario fue actualizado correctamente"));
			}
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Eliminar");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (ExceptionMessage e) {
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
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

	public void listaUsuarios()  {
		list = iUsuariosService.getAll();
	}

	public void actualizarPerfil() throws Exception {
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		try {
			iUsuariosService.updateProfile(usuario);
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo("Actualizacion Perfil");
			auditoria.setAccion(Mensajes.USUARIO + nombreUsuario + " actualizo un registro");
			auditoria.setNivel("INFO");
			auditoriaService.registroAuditoria(auditoria);
		} catch (Exception e) {
			e.getMessage();
			Auditoria auditoria = new Auditoria();
			auditoria.setFechaAuditoria(new Date());
			auditoria.setIdUsuario(idUsuario);
			auditoria.setClaseOrigen(this.getClass().getName());
			auditoria.setMetodo(String.valueOf(Mensajes.ERROR));
			auditoria.setAccion(String.valueOf(Mensajes.ERROR) + e.getMessage());
			auditoria.setNivel(String.valueOf(Mensajes.ERROR));
			auditoriaService.registroAuditoria(auditoria);
		}

	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
