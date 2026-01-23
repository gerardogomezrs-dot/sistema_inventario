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

        Usuario user = (session != null) ? (Usuario) session.getAttribute("sessionUsuario") : null;

        if (user != null) {
            chain.doFilter(request, response); // Usuario autenticado, adelante
        } else {
            res.sendRedirect(req.getContextPath() + "/login.xhtml"); // No autenticado
        }
    }
}