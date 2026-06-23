package com.sena.database_connection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.database_connection.exception.NegocioException;
import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    // Buscar usuario por id
    public Usuario buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new NegocioException("No se encontro un usuario con id " + id, 404);
        }
        return usuario;
    }

    // Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {

        // Validar que el documento no este en uso
        Usuario existeDocumento = usuarioRepository.findByDocumento(usuario.getDocumento());
        if (existeDocumento != null) {
            throw new NegocioException("Ya existe un usuario con el documento " + usuario.getDocumento(), 400);
        }

        // Validar que el correo no este en uso
        Usuario existeCorreo = usuarioRepository.findByCorreo(usuario.getCorreo());
        if (existeCorreo != null) {
            throw new NegocioException("Ya existe un usuario con el correo " + usuario.getCorreo(), 400);
        }

        // El usuario se crea activo por defecto
        usuario.setActivo(true);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return usuarioGuardado;
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new NegocioException("No se encontro un usuario con id " + id, 404);
        }

        usuario.setNombreCompleto(datosNuevos.getNombreCompleto());
        usuario.setCorreo(datosNuevos.getCorreo());
        usuario.setDocumento(datosNuevos.getDocumento());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioActualizado;
    }

    // Desactivar un usuario (no se elimina fisicamente)
    public Usuario desactivarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new NegocioException("No se encontro un usuario con id " + id, 404);
        }

        if (usuario.isActivo() == false) {
            throw new NegocioException("El usuario ya esta desactivado", 400);
        }

        usuario.setActivo(false);
        Usuario usuarioDesactivado = usuarioRepository.save(usuario);

        return usuarioDesactivado;
    }
}