package com.sena.database_connection.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sena.database_connection.exception.NegocioException;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.enums.EstadoReserva;
import com.sena.database_connection.repositories.AmbienteRepository; // IMPORTANTE
import com.sena.database_connection.repositories.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final AmbienteRepository ambienteRepository; // Inyección añadida

    private static final LocalTime HORA_MINIMA = LocalTime.of(6, 0);
    private static final LocalTime HORA_MAXIMA = LocalTime.of(22, 0);

    // Constructor actualizado con ambos repositorios
    ReservaService(ReservaRepository reservaRepository, AmbienteRepository ambienteRepository) {
        this.reservaRepository = reservaRepository;
        this.ambienteRepository = ambienteRepository;
    }

    public Reserva crearReserva(Reserva reserva) {
        LocalDateTime inicio = reserva.getFechaHoraInicio();
        LocalDateTime fin = reserva.getFechaHoraFin();

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new NegocioException("La fecha de inicio no puede estar en el pasado", 400);
        }

        if (reserva.getAmbiente() == null || reserva.getAmbiente().getId() == null) {
            throw new NegocioException("Debe seleccionar un ambiente válido para la reserva", 400);
        }

        // 🔥 SOLUCIÓN: Traer el ambiente real y completo desde la Base de Datos
        Ambiente ambiente = ambienteRepository.findById(reserva.getAmbiente().getId())
                .orElseThrow(() -> new NegocioException("El ambiente seleccionado no existe en el sistema", 404));

        if (ambiente.getActivo() == null || !ambiente.getActivo()) {
            throw new NegocioException("El ambiente seleccionado no está activo", 400);
        }

        if (reserva.getNumeroAprendices() > ambiente.getCapacidad()) {
            throw new NegocioException("El número de aprendices supera la capacidad del ambiente (" + ambiente.getCapacidad() + ")", 400);
        }

        if (!inicio.toLocalDate().isEqual(fin.toLocalDate())) {
            throw new NegocioException("La reserva debe iniciar y terminar el mismo día", 400);
        }

        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();

        if (horaInicio.isBefore(HORA_MINIMA)) {
            throw new NegocioException("La reserva no puede iniciar antes de las 06:00", 400);
        }
        if (horaFin.isAfter(HORA_MAXIMA)) {
            throw new NegocioException("La reserva no puede terminar después de las 22:00", 400);
        }

        long minutosDuracion = Duration.between(inicio, fin).toMinutes();

        if (minutosDuracion < 60) {
            throw new NegocioException("La reserva debe durar al menos 1 hora", 400);
        }
        if (minutosDuracion > 240) {
            throw new NegocioException("La reserva no puede durar más de 4 horas", 400);
        }

        LocalDate diaReserva = inicio.toLocalDate();
        List<Reserva> reservasInstructor = reservaRepository.findByNombreInstructorAndEstado(
                reserva.getNombreInstructor(), EstadoReserva.ACTIVA);

        long contadorDia = reservasInstructor.stream()
                .filter(r -> r.getFechaHoraInicio().toLocalDate().isEqual(diaReserva))
                .count();

        if (contadorDia >= 3) {
            throw new NegocioException("El instructor ya tiene 3 reservas activas el día " + diaReserva, 400);
        }

        List<Reserva> solapadas = reservaRepository.findSolapadas(ambiente.getId(), inicio, fin);

        if (!solapadas.isEmpty()) {
            throw new NegocioException("El ambiente ya tiene una reserva activa en ese horario", 409);
        }

        reserva.setAmbiente(ambiente); // Vinculamos el ambiente mapeado completo
        reserva.setEstado(EstadoReserva.ACTIVA);
        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("No se encontró una reserva con el id " + id, 404));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new NegocioException("La reserva ya está cancelada", 400);
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(reserva.getFechaHoraInicio())) {
            throw new NegocioException("No se puede cancelar una reserva que ya inició", 400);
        }

        long minutosFaltantes = Duration.between(ahora, reserva.getFechaHoraInicio()).toMinutes();

        if (minutosFaltantes < 120) {
            throw new NegocioException("Solo se puede cancelar con al menos 2 horas de anticipación", 400);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }

    // Obtener todo el historial de reservas
    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    // Buscar reservas activas de un instructor (usando el método que ya tienes en tu repo)
    public List<Reserva> obtenerPorInstructor(String nombre) {
        return reservaRepository.findByNombreInstructorAndEstado(nombre, EstadoReserva.ACTIVA);
    }
}