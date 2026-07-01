package com.sena.database_connection.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.dto.ReservaRequestDTO;
import com.sena.database_connection.model.dto.ReservaResponseDTO;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        Reserva reserva = new Reserva();
        reserva.setFechaHoraInicio(request.getFechaHoraInicio());
        reserva.setFechaHoraFin(request.getFechaHoraFin());
        reserva.setNumeroAprendices(request.getNumeroAprendices());
        Ambiente a = new Ambiente();
        a.setId(request.getAmbienteId());
        reserva.setAmbiente(a);
        Usuario u = new Usuario();
        u.setId(request.getInstructorId());
        reserva.setInstructor(u);

        Reserva creada = reservaService.crearReserva(reserva);
        return new ResponseEntity<>(ReservaResponseDTO.fromEntity(creada), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        Reserva cancelada = reservaService.cancelarReserva(id);
        return new ResponseEntity<>(ReservaResponseDTO.fromEntity(cancelada), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        List<ReservaResponseDTO> reservas = reservaService.obtenerTodas()
            .stream()
            .map(ReservaResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerPorId(id);
        return ResponseEntity.ok(ReservaResponseDTO.fromEntity(reserva));
    }

    @GetMapping("/instructor/{nombre}")
    public ResponseEntity<List<ReservaResponseDTO>> listarPorInstructor(@PathVariable String nombre) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerPorInstructor(nombre)
            .stream()
            .map(ReservaResponseDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(reservas);
    }
}
