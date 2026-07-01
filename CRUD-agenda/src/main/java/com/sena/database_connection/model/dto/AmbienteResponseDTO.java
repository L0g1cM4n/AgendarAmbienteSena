package com.sena.database_connection.model.dto;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.enums.TipoAmbiente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbienteResponseDTO {

    private Long id;
    private String nombre;
    private TipoAmbiente tipo;
    private Integer capacidad;
    private Boolean activo;

    public static AmbienteResponseDTO fromEntity(Ambiente a) {
        return new AmbienteResponseDTO(
            a.getId(),
            a.getNombre(),
            a.getTipo(),
            a.getCapacidad(),
            a.getActivo()
        );
    }
}
