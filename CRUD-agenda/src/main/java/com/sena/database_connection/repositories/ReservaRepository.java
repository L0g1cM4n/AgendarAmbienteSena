package com.sena.database_connection.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.model.enums.EstadoReserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Busca reservas activas del mismo ambiente que se solapen con el nuevo horario
    @Query("SELECT r FROM Reserva r WHERE r.ambiente.id = :ambienteId " +
        "AND r.estado = 'ACTIVA' " +
        "AND (:inicio < r.fechaHoraFin AND :fin > r.fechaHoraInicio)")
    List<Reserva> findSolapadas(@Param("ambienteId") Long ambienteId,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fin") LocalDateTime fin);

    List<Reserva> findByInstructorAndEstado(Usuario instructor, EstadoReserva estado);

    List<Reserva> findByInstructorNombreCompletoContainingIgnoreCase(String nombre);

    @Query("SELECT r FROM Reserva r WHERE r.ambiente.id = :ambienteId AND r.estado = 'ACTIVA' " +
        "AND r.fechaHoraInicio >= :inicio AND r.fechaHoraInicio <= :fin")
    List<Reserva> findByAmbienteIdAndFecha(
        @Param("ambienteId") Long ambienteId, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin
    );

    @Query("SELECT r FROM Reserva r WHERE r.estado = 'ACTIVA' " +
        "AND r.fechaHoraInicio >= :inicio AND r.fechaHoraInicio < :fin")
    List<Reserva> findActivasEnRango(
        @Param("inicio") LocalDateTime inicio,
        @Param("fin") LocalDateTime fin
    );
}