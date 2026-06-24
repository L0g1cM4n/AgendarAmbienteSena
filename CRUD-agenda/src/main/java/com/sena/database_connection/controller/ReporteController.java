package com.sena.database_connection.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.service.AmbienteService;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private AmbienteService ambienteService;

    @GetMapping("/ocupacion")
    public ResponseEntity<List<Map<String, Object>>> obtenerReporteOcupacion(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        List<Ambiente> ambientes = ambienteService.listarTodos();
        List<Map<String, Object>> reporte = new ArrayList<>();

        for (Ambiente amb : ambientes) {
            // Conseguir las reservas de este ambiente en el día seleccionado
            List<Reserva> reservasDelDia = ambienteService.obtenerReservasPorAmbienteYFecha(amb.getId(), fecha);
            
            long minutosReservados = 0;
            for (Reserva res : reservasDelDia) {
                minutosReservados += Duration.between(res.getFechaHoraInicio(), res.getFechaHoraFin()).toMinutes();
            }

            double horasReservadas = minutosReservados / 60.0;
            double porcentajeOcupacion = (horasReservadas / 16.0) * 100.0;

            // Armamos el JSON estructurado para Postman
            Map<String, Object> filaReporte = new HashMap<>();
            filaReporte.put("ambienteId", amb.getId());
            filaReporte.put("ambienteNombre", amb.getNombre());
            filaReporte.put("horasReservadas", horasReservadas);
            filaReporte.put("porcentajeOcupacion", Math.round(porcentajeOcupacion * 100.0) / 100.0 + "%");

            reporte.add(filaReporte);
        }

        return ResponseEntity.ok(reporte);
    }
}