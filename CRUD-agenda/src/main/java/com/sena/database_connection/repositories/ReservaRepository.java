package com.sena.database_connection.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.enums.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    
    @Query("SELECT r FROM Reserva r WHERE r.ambiente.id = :ambienteId " +
        "AND r.estado = 'ACTIVA' " +
        "AND (:inicio < r.fechaHoraFin AND :fin > r.fechaHoraInicio)")
    List<Reserva> findSolapadas(@Param("ambienteId") Long ambienteId,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fin") LocalDateTime fin);

  
    List<Reserva> findByNombreInstructorAndEstado(String nombreInstructor, EstadoReserva estado);
}