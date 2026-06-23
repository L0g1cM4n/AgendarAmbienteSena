package com.sena.database_connection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET /api/usuarios - Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // GET /api/usuarios/{id} - Buscar usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    // POST /api/usuarios - Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // PUT /api/usuarios/{id} - Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario datosNuevos) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, datosNuevos);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }

    // PATCH /api/usuarios/{id}/desactivar - Desactivar un usuario
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        Usuario usuarioDesactivado = usuarioService.desactivarUsuario(id);
        return new ResponseEntity<>(usuarioDesactivado, HttpStatus.OK);
    }
}