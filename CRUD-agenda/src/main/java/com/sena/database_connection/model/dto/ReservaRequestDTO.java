package com.sena.database_connection.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "Debe seleccionar un ambiente")
    private Long ambienteId;

    @NotNull(message = "Debe seleccionar un instructor")
    private Long instructorId;

    @Min(value = 1, message = "Debe haber al menos 1 aprendiz")
    private int numeroAprendices;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private LocalDateTime fechaHoraInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaHoraFin;
}
