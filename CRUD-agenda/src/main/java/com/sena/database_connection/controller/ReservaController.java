package com.sena.database_connection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.ReservaService;

// Los errores ya no se manejan aqui con try-catch
// El GlobalExceptionHandler los captura automaticamente para todos los controladores
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // POST /api/reservas - Crear una nueva reserva
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.crearReserva(reserva);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    // PATCH /api/reservas/{id}/cancelar - Cancelar una reserva existente
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return new ResponseEntity<>(reservaCancelada, HttpStatus.OK);
    }
}