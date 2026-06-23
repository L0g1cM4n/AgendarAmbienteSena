package com.sena.database_connection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.database_connection.model.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su numero de documento
    Usuario findByDocumento(String documento);

    // Busca un usuario por su correo
    Usuario findByCorreo(String correo);
}