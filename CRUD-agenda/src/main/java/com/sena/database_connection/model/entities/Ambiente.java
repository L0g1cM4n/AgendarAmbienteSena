package com.sena.database_connection.model.entities;

import com.sena.database_connection.model.enums.TipoAmbiente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ambientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del ambiente es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de ambiente es obligatorio")
    @Column(nullable = false)
    private TipoAmbiente tipo;

    @Min(value = 1, message = "La capacidad debe ser al menos de 1 persona")
    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private boolean activo = true;
}