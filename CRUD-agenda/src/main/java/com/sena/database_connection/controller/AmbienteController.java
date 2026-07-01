package com.sena.database_connection.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.dto.AmbienteRequestDTO;
import com.sena.database_connection.model.dto.AmbienteResponseDTO;
import com.sena.database_connection.model.dto.ReservaResponseDTO;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.service.AmbienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    @Autowired
    private AmbienteService ambienteService;

    @GetMapping
    public ResponseEntity<List<AmbienteResponseDTO>> listar() {
        List<Ambiente> ambientes = ambienteService.listarTodos();
        List<AmbienteResponseDTO> dto = ambientes.stream()
            .map(AmbienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AmbienteResponseDTO> crear(@Valid @RequestBody AmbienteRequestDTO request) {
        Ambiente ambiente = new Ambiente();
        ambiente.setNombre(request.getNombre());
        ambiente.setTipo(request.getTipo());
        ambiente.setCapacidad(request.getCapacidad());
        Ambiente guardado = ambienteService.guardar(ambiente);
        return new ResponseEntity<>(AmbienteResponseDTO.fromEntity(guardado), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ambienteService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponseDTO>> verReservasPorFecha(
            @PathVariable Long id,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<ReservaResponseDTO> reservas = ambienteService.obtenerReservasPorAmbienteYFecha(id, fecha)
            .stream()
            .map(ReservaResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<AmbienteResponseDTO>> listarDisponibles(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<AmbienteResponseDTO> disponibles = ambienteService.listarDisponibles(inicio, fin)
            .stream()
            .map(AmbienteResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(disponibles);
    }
}
