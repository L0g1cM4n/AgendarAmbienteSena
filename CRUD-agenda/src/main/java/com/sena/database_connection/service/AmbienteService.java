package com.sena.database_connection.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.database_connection.exception.NegocioException;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.repositories.AmbienteRepository;
import com.sena.database_connection.repositories.ReservaRepository;

@Service
public class AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Ambiente> listarTodos() {
        return ambienteRepository.findAll();
    }

    public Ambiente guardar(Ambiente ambiente) {
        ambiente.setActivo(true);
        return ambienteRepository.save(ambiente);
    }

public void eliminarLogico(Long id) {
        Ambiente amb = ambienteRepository.findById(id).orElse(null);
        if (amb == null) {
            throw new NegocioException("Ambiente no encontrado con el ID: " + id, HttpStatus.NOT_FOUND.value());
        }
        amb.setActivo(false);
        ambienteRepository.save(amb);
    }

    public List<Reserva> obtenerReservasPorAmbienteYFecha(Long ambienteId, java.time.LocalDate fecha) {
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);
        return reservaRepository.findByAmbienteIdAndFecha(ambienteId, inicioDia, finDia);
    }

    public List<Ambiente> listarDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        List<Ambiente> activos = ambienteRepository.findAll().stream()
                .filter(Ambiente::getActivo)
                .collect(java.util.stream.Collectors.toList());

        return activos.stream()
                .filter(amb -> reservaRepository.findSolapadas(amb.getId(), inicio, fin).isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }
}