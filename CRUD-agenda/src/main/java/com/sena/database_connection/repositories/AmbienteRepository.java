package com.sena.database_connection.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sena.database_connection.model.entities.Ambiente;


public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {
}