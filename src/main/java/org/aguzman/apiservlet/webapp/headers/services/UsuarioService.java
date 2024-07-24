package org.aguzman.apiservlet.webapp.headers.services;

import org.aguzman.apiservlet.webapp.headers.models.Producto;
import org.aguzman.apiservlet.webapp.headers.models.Usuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> login(String username, String password);
    List<Usuario> listar();
    Optional<Usuario> porId(Long id) throws SQLException;
    void guardar(Usuario usuario);
    void eliminar(Long id);

}
