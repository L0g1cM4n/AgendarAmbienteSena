package com.sena.database_connection.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.exception.NegocioException;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.ReservaService;

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

    // 🔥 Capturador de emergencia local
    @org.springframework.web.bind.annotation.ExceptionHandler(NegocioException.class)
    public ResponseEntity<com.sena.database_connection.exception.ErrorResponse> manejarNegocioExceptionLocal(NegocioException ex) {
        
        com.sena.database_connection.exception.ErrorResponse error = new com.sena.database_connection.exception.ErrorResponse(
            ex.getStatus(),
            "Error de Regla de Negocio (Local)",
            ex.getMessage(),
            java.time.LocalDateTime.now()
        );
        
        org.springframework.http.HttpStatus statusHttp = org.springframework.http.HttpStatus.resolve(ex.getStatus());
        if (statusHttp == null) {
            statusHttp = org.springframework.http.HttpStatus.BAD_REQUEST;
        }
        
        return new ResponseEntity<>(error, statusHttp);
    }
}