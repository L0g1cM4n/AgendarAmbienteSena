package com.sena.database_connection.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.database_connection.model.entities.Ambiente;

@Repository
public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {
    // Para listar solo los ambientes que estén activos en el SENA
    List<Ambiente> findByActivoTrue();
}