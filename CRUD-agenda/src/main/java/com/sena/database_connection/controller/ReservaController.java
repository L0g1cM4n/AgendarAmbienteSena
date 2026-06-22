package com.sena.database_connection.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // POST /api/reservas - Crear una nueva reserva
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.crearReserva(reserva);
            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
        } catch (NegocioException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getStatus()));
        }
    }

    // PATCH /api/reservas/{id}/cancelar - Cancelar una reserva existente
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            Reserva reservaCancelada = reservaService.cancelarReserva(id);
            return new ResponseEntity<>(reservaCancelada, HttpStatus.OK);
        } catch (NegocioException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.valueOf(ex.getStatus()));
        }
    }
}