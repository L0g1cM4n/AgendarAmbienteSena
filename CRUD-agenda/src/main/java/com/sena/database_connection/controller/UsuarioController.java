package com.sena.database_connection.controller;

import java.util.List;

import jakarta.validation.Valid;
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

import com.sena.database_connection.model.dto.UsuarioRequestDTO;
import com.sena.database_connection.model.dto.UsuarioResponseDTO;
import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos()
            .stream()
            .map(UsuarioResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario usuario = new Usuario();
        usuario.setDocumento(request.getDocumento());
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setCorreo(request.getCorreo());
        Usuario creado = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(UsuarioResponseDTO.fromEntity(creado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO request) {
        Usuario datos = new Usuario();
        datos.setDocumento(request.getDocumento());
        datos.setNombreCompleto(request.getNombreCompleto());
        datos.setCorreo(request.getCorreo());
        Usuario actualizado = usuarioService.actualizarUsuario(id, datos);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(actualizado));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponseDTO> desactivarUsuario(@PathVariable Long id) {
        Usuario desactivado = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(desactivado));
    }
}
