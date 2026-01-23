package com.empresa.inventario.utils; 
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

import com.empresa.inventario.model.Usuario;


@WebFilter("")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        Usuario user = (session != null)
                ? (Usuario) session.getAttribute("usuario")
                : null;

        if (user == null) {
            ((HttpServletResponse) response)
                    .sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}
