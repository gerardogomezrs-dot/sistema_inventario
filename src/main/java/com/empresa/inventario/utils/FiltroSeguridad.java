package com.empresa.inventario.utils;

import com.empresa.inventario.model.Usuario;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
@WebFilter(urlPatterns = { "/pages/*" })
public class FiltroSeguridad implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String url = req.getRequestURI();

        Usuario user = (session != null) ? (Usuario) session.getAttribute("sessionUsuario") : null;

        // 1. Verificación de Autenticación
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return; // Detiene la ejecución para evitar procesar el resto del código con un usuario nulo
        }

        // 2. Lógica de protección por carpeta (Solo se ejecuta si user != null)
        if (url.contains("/pages/admin/") && !user.getRol().equals("admin")) {
            res.sendRedirect(req.getContextPath() + "/error_permisos.xhtml");
            return; // Evita que se llame a chain.doFilter() después de redireccionar
        } 
        else if (url.contains("/pages/almacen/") && !user.getRol().equals("almacen")) {
            res.sendRedirect(req.getContextPath() + "/error_permisos.xhtml");
            return; // Evita que se llame a chain.doFilter() después de redireccionar
        } 
        else {
            // Si el rol coincide con la carpeta o es una página permitida dentro de /pages/
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}