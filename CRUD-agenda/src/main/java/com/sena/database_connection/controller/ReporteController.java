package com.sena.database_connection.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.dto.AmbienteMasUsadoDTO;
import com.sena.database_connection.model.dto.ReporteOcupacionDTO;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.AmbienteService;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final AmbienteService ambienteService;

    @GetMapping("/ocupacion")
    public ResponseEntity<List<ReporteOcupacionDTO>> obtenerReporteOcupacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<Ambiente> ambientes = ambienteService.listarTodos();
        List<ReporteOcupacionDTO> reporte = new ArrayList<>();

        for (Ambiente amb : ambientes) {
            List<Reserva> reservasDelDia = ambienteService.obtenerReservasPorAmbienteYFecha(amb.getId(), fecha);

            long minutosReservados = 0;
            for (Reserva res : reservasDelDia) {
                minutosReservados += Duration.between(res.getFechaHoraInicio(), res.getFechaHoraFin()).toMinutes();
            }

            double horasReservadas = minutosReservados / 60.0;
            double porcentajeOcupacion = (horasReservadas / 16.0) * 100.0;

            reporte.add(new ReporteOcupacionDTO(
                amb.getId(),
                amb.getNombre(),
                horasReservadas,
                Math.round(porcentajeOcupacion * 100.0) / 100.0 + "%"
            ));
        }

        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/ambiente-mas-usado")
    public ResponseEntity<AmbienteMasUsadoDTO> obtenerAmbienteMasUsado(
            @RequestParam(value = "semana", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semana) {

        LocalDate fechaFin = semana != null ? semana : LocalDate.now();
        Map.Entry<Ambiente, Double> resultado = ambienteService.calcularAmbienteMasUsado(fechaFin);

        AmbienteMasUsadoDTO dto = new AmbienteMasUsadoDTO(
            resultado.getKey().getId(),
            resultado.getKey().getNombre(),
            resultado.getKey().getTipo().name(),
            resultado.getKey().getCapacidad(),
            Math.round(resultado.getValue() * 100.0) / 100.0
        );
        return ResponseEntity.ok(dto);
    }
}
