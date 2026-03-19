package com.empresa.inventario.beans;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.LoggerFactory;

import com.empresa.inventario.exceptions.ExceptionMessage;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.IAuditoriaService;
import com.empresa.inventario.service.IAuthService;
import com.empresa.inventario.utils.Mensajes;

import lombok.Data;

@Named("loginBean")
@SessionScoped
@Data
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LoginBean.class);

    // Llaves y mensajes (sin transient para evitar que se vuelvan null)
    private static final String SESSION_USER_KEY = "sessionUsuario";
    private final String mensajeBienvenida1 = "¡Bienvenido!";
    private final String mensajeBienvenida2 = ", has iniciado sesión correctamente.";
    private final String mensajeBienvenida3 = "Hola ";

    private String userName;
    private String password;
    private Usuario usuario;
    private int idUsuario;
    private String nombreUsuario;

    private IAuditoriaService auditoriaService;
    private IAuthService authService;
    private BaseAuditoriaBean auditoriaBean = new BaseAuditoriaBean();

    @Inject
    public LoginBean(IAuthService authService, IAuditoriaService auditoriaService) {
        this.authService = authService;
        this.auditoriaService = auditoriaService;
    }

    public String login() {
        try {
            usuario = authService.login(userName, password);
            
            if (usuario == null) {
                resetearSesion();
                mensaje(FacesMessage.SEVERITY_ERROR, "Error de Inventario", "Usuario o password inválido");
                return null;
            }

            // 1. Guardar en sesión y preparar mensajes (Código común)
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(SESSION_USER_KEY, usuario);
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            
            mensaje(FacesMessage.SEVERITY_INFO, mensajeBienvenida1,
                    mensajeBienvenida3 + usuario.getNombre() + mensajeBienvenida2);

            // 2. Determinar ruta según el rol
            String ruta = determinarRutaPorRol(usuario.getRol());

            // 3. Auditoría y datos de instancia
            idUsuario = usuario.getIdUsuario();
            nombreUsuario = usuario.getNombre();
            auditoriaBean = new BaseAuditoriaBean();
            auditoriaBean.registrarAuditoria(auditoriaService, "INICIO DE SESION",
                    Mensajes.USUARIO + nombreUsuario + "INICIO SESION ", Mensajes.INFO.toString(), idUsuario);

            return ruta;

        } catch (ExceptionMessage e) {
            logger.debug(e.getMessage());
            mensaje(FacesMessage.SEVERITY_ERROR, "Error:", e.getMessage());
            auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
                    Mensajes.ERROR.toString(), idUsuario);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            mensaje(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Ocurrió un error en el servidor. ");
            logger.debug(e.getMessage());
            auditoriaBean.registrarAuditoria(auditoriaService, Mensajes.ERROR, Mensajes.ERROR + ": " + e.getMessage(),
                    Mensajes.ERROR.toString(), idUsuario);
            return null;
        }
    }

    private String determinarRutaPorRol(String rol) {
        switch (rol) {
            case "admin":
                return "/pages/admin/dashboard.xhtml?faces-redirect=true";
            case "stock_manager":
                return "/pages/stock_manager/dashboard.xhtml?faces-redirect=true";
            case "almacen":
                return "/pages/almacen/dashboard.xhtml?faces-redirect=true";
            default:
                return null;
        }
    }

    private void mensaje(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private void resetearSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
}