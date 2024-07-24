package org.aguzman.apiservlet.webapp.headers.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aguzman.apiservlet.webapp.headers.models.Usuario;
import org.aguzman.apiservlet.webapp.headers.services.UsuarioService;
import org.aguzman.apiservlet.webapp.headers.services.UsuarioServiceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/usuarios/form")
public class UsuarioFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");
        UsuarioService service = new UsuarioServiceImpl(conn);
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }
        Usuario usuario = new Usuario();

        if (id > 0) {
            Optional<Usuario> o;
            try {
                o = service.porId(id);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (o.isPresent()) {
                usuario = o.get();
            }
        }

        req.setAttribute("usuario", usuario);
        req.setAttribute("title", " Registro de usuario");

        getServletContext().getRequestDispatcher("/formUsuario.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = (Connection) req.getAttribute("conn");
        UsuarioService service = new UsuarioServiceImpl(conn);
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        Map<String,String> errores = new HashMap<>();

        if (username == null || username.isEmpty()) {
            errores.put("username", "username no puede ser vacio");
        }

        if (password == null || password.isEmpty()) {
            errores.put("password", "password no puede ser vacio");
        }

        if (email == null || email.isEmpty()) {
            errores.put("email", "email no puede ser vacio");
        }


            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setEmail(email);


        if (errores.isEmpty()) {
            service.guardar(usuario);
            resp.sendRedirect(req.getContextPath() + "/usuarios");
        } else {
            req.setAttribute("errores", errores);
            req.setAttribute("usuario", usuario);
            req.setAttribute("title", " Formulario de usuarios");
            getServletContext().getRequestDispatcher("/formUsuario.jsp").forward(req, resp);
        }
    }
}
