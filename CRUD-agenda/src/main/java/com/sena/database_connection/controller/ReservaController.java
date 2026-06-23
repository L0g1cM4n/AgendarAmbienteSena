package com.sena.database_connection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.ReservaService;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        return new ResponseEntity<>(reservaService.crearReserva(reserva), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        return new ResponseEntity<>(reservaService.cancelarReserva(id), HttpStatus.OK);
    }

    // 1. GET para listar todas las reservas: http://localhost:8080/api/reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        return ResponseEntity.ok(reservaService.obtenerTodas());
    }

    // 2. GET para buscar por instructor: http://localhost:8080/api/reservas/instructor/{nombre}
    @GetMapping("/instructor/{nombre}")
    public ResponseEntity<List<Reserva>> listarPorInstructor(@PathVariable String nombre) {
        return ResponseEntity.ok(reservaService.obtenerPorInstructor(nombre));
    }
}