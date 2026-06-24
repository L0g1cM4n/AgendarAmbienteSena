package com.sena.database_connection.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.AmbienteService;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    @Autowired
    private AmbienteService ambienteService;

    @GetMapping
    public ResponseEntity<List<Ambiente>> listar() { 
        return ResponseEntity.ok(ambienteService.listarTodos()); 
    }

    @PostMapping
    public ResponseEntity<Ambiente> crear(@RequestBody Ambiente ambiente) {
        return new ResponseEntity<>(ambienteService.guardar(ambiente), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ambienteService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    // 1. Ver las reservas activas de un ambiente en una fecha (?fecha=2026-06-15)
    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<Reserva>> verReservasPorFecha(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam("fecha") 
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate fecha) {
        return ResponseEntity.ok(ambienteService.obtenerReservasPorAmbienteYFecha(id, fecha));
    }

    // 2. Listar los ambientes libres en un rango de tiempo dado (?inicio=...&fin=...)
    @GetMapping("/disponibles")
    public ResponseEntity<List<Ambiente>> listarDisponibles(
            @org.springframework.web.bind.annotation.RequestParam("inicio") 
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime inicio,
            @org.springframework.web.bind.annotation.RequestParam("fin") 
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime fin) {
        return ResponseEntity.ok(ambienteService.listarDisponibles(inicio, fin));
    }
}