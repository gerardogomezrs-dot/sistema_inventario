package com.empresa.inventario.beans.admin;

import java.io.Serializable;
import javax.faces.view.ViewScoped; 

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.beans.BaseAuditoriaBean;
import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IUsuariosService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("usuariosBean")
@ViewScoped
@Data
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IUsuariosService iUsuariosService;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UsuariosBean.class);
	
	private transient List<Usuario> list;

	private transient Usuario usuario;

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
		usuario = new Usuario();
	}

	public String irANuevoUsuario() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		baseBean.registrarNavegacion(auditoriaService, Mensajes.NUEVO_REGISTRO, "entro a nuevo usuario", idUsuario,
				nombreUsuario);
		return "/pages/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String irATablaUsuario() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Tabla Usuario", "entro a tabla Usuario", idUsuario,
				nombreUsuario);

		return "/pages/admin/usuarios/tablaUsuarios.xhtml?faces-redirect=true";
	}

	public String irADashboard() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		baseBean.registrarNavegacion(auditoriaService, "Dashboard", "entro a Dashboard", idUsuario, nombreUsuario);

		return "/pages/admin/dashboard?faces-redirect=true";
	}

	public void guardar() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			iUsuariosService.save(usuario);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.GUARDAR,
					Mensajes.USUARIO + nombreUsuario + "realizo el guardado de un registro", Mensajes.INFO.toString(),
					idUsuario);

		} catch (ExceptionMessage e) {
			logger.debug(e.getMessage());
			mensaje(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Consulte al administrador.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.validationFailed();
			context.renderResponse();
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void eliminar() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		try {
			iUsuariosService.delete(usuario.getIdUsuario(), idUsuario);
			list = iUsuariosService.getAll();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario eliminado", "El usuario fue eliminado correctamente"));
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ELIMINAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una eliminacion", Mensajes.INFO.toString(), idUsuario);

		} catch (ExceptionMessage e) {
			logger.debug(e.getMessage());
			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	public void actualizar() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();

		try {
			if (usuario == null) {
				throw new ExceptionMessage("Vacio");
			} else {
				iUsuariosService.update(usuario);
				list = iUsuariosService.getAll();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Usuario actualizado", "El usuario fue actualizado correctamente"));
			}
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);
		} catch (ExceptionMessage e) {
			logger.debug(e.getMessage());

			mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}

	}

	public void listaUsuarios() {
		list = iUsuariosService.getAll();
	}

	public void actualizarPerfil() {
		 BaseAuditoriaBean baseBean = new BaseAuditoriaBean();
		if (usuario == null) {
			throw new ExceptionMessage("Vacio");
		}
		try {
			iUsuariosService.updateProfile(usuario);
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ACTUALIZAR.getTexto(),
					Mensajes.USUARIO + nombreUsuario + " realizo una actualizacion", Mensajes.INFO.toString(),
					idUsuario);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			baseBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
					Mensajes.ERROR.toString(), idUsuario);
		}
	}

	private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}
}
