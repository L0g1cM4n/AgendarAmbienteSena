package com.sena.database_connection.model.dto;

import com.sena.database_connection.model.enums.TipoAmbiente;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbienteRequestDTO {

    @NotBlank(message = "El nombre del ambiente es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de ambiente es obligatorio")
    private TipoAmbiente tipo;

    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    private Integer capacidad;
}
