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

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        if (url.contains("/pages/admin/") && !user.getRol().equals("admin")) {
            res.sendRedirect(req.getContextPath() + "/error_permisos.xhtml");
            return;
        } 
        else if (url.contains("/pages/almacen/") && !user.getRol().equals("almacen")) {
            res.sendRedirect(req.getContextPath() + "/error_permisos.xhtml");
            return; 
        } 
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}