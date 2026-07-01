package com.sena.database_connection.model.dto;

import java.time.LocalDateTime;

import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.enums.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private Long id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private int numeroAprendices;
    private EstadoReserva estado;
    private AmbienteResponseDTO ambiente;
    private UsuarioResponseDTO instructor;

    public static ReservaResponseDTO fromEntity(Reserva r) {
        return new ReservaResponseDTO(
            r.getId(),
            r.getFechaHoraInicio(),
            r.getFechaHoraFin(),
            r.getNumeroAprendices(),
            r.getEstado(),
            AmbienteResponseDTO.fromEntity(r.getAmbiente()),
            UsuarioResponseDTO.fromEntity(r.getInstructor())
        );
    }
}
