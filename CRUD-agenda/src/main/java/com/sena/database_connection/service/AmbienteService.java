package com.sena.database_connection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.repositories.AmbienteRepository;
import com.sena.database_connection.repositories.ReservaRepository;
import com.sena.database_connection.exception.NegocioException;

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
        ambiente.setActivo(true); // Todo ambiente nuevo inicia activo
        return ambienteRepository.save(ambiente);
    }

public void eliminarLogico(Long id) {
        Ambiente amb = ambienteRepository.findById(id).orElse(null);
        if (amb == null) {
            // Se envía el HttpStatus.NOT_FOUND y luego el texto
            throw new NegocioException("Ambiente no encontrado con el ID: " + id, HttpStatus.NOT_FOUND.value());
        }
        amb.setActivo(false); // Desactivación lógica
        ambienteRepository.save(amb);
    }

    // ENDPOINT REQUERIDO 1: Ver reservas de un ambiente en una fecha específica
    public List<Reserva> obtenerReservasPorAmbienteYFecha(Long ambienteId, java.time.LocalDate fecha) {
        // Definimos el inicio y fin del día completo (00:00:00 a 23:59:59)
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);
        return reservaRepository.findByAmbienteIdAndFecha(ambienteId, inicioDia, finDia);
    }

    // ENDPOINT REQUERIDO 2: Listar ambientes disponibles que NO se solapen en un horario dado
    public List<Ambiente> listarDisponibles(LocalDateTime inicio, LocalDateTime fin) {
        // 1. Traemos todos los ambientes que estén activos en el sistema
        List<Ambiente> activos = ambienteRepository.findAll().stream()
                .filter(Ambiente::getActivo)
                .collect(java.util.stream.Collectors.toList());

        // 2. Filtramos: nos quedamos solo con los que NO tengan reservas solapadas en ese rango
        return activos.stream()
                .filter(amb -> reservaRepository.findSolapadas(amb.getId(), inicio, fin).isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }
}