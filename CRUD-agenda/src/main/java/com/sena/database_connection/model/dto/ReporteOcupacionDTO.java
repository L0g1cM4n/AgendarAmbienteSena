package com.sena.database_connection.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteOcupacionDTO {

    private Long ambienteId;
    private String ambienteNombre;
    private double horasReservadas;
    private String porcentajeOcupacion;
}
