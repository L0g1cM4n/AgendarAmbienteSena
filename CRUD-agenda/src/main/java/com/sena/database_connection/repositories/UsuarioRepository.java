package com.sena.database_connection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.database_connection.model.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByDocumento(String documento);

    Usuario findByCorreo(String correo);
}