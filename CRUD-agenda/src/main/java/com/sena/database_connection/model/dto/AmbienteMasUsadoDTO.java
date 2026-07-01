package com.sena.database_connection.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AmbienteMasUsadoDTO {

    private Long id;
    private String nombre;
    private String tipo;
    private Integer capacidad;
    private double totalHorasReservadas;
}
