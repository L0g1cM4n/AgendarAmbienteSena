package com.sena.database_connection.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.database_connection.exception.NegocioException;
import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.model.entities.Reserva;
import com.sena.database_connection.model.entities.Usuario;
import com.sena.database_connection.model.enums.EstadoReserva;
import com.sena.database_connection.repositories.AmbienteRepository;
import com.sena.database_connection.repositories.ReservaRepository;
import com.sena.database_connection.repositories.UsuarioRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AmbienteRepository ambienteRepository;

    private static final LocalTime HORA_MINIMA = LocalTime.of(6, 0);
    private static final LocalTime HORA_MAXIMA = LocalTime.of(22, 0);

    public Reserva crearReserva(Reserva reserva) {

        LocalDateTime inicio = reserva.getFechaHoraInicio();
        LocalDateTime fin = reserva.getFechaHoraFin();

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new NegocioException("La fecha de inicio no puede estar en el pasado", 400);
        }

        if (reserva.getAmbiente() == null || reserva.getAmbiente().getId() == null) {
            throw new NegocioException("Debe seleccionar un ambiente para la reserva", 400);
        }

        Ambiente ambienteReal = ambienteRepository.findById(reserva.getAmbiente().getId())
                .orElseThrow(() -> new NegocioException("El ambiente seleccionado no existe en el sistema", 404));

        if (ambienteReal.getActivo() == false) {
            throw new NegocioException("El ambiente seleccionado no esta activo", 400);
        }

        if (reserva.getNumeroAprendices() > ambienteReal.getCapacidad()) {
            throw new NegocioException("El numero de aprendices supera la capacidad del ambiente (" + ambienteReal.getCapacidad() + ")", 400);
        }

        if (inicio.toLocalDate().isEqual(fin.toLocalDate()) == false) {
            throw new NegocioException("La reserva debe iniciar y terminar el mismo dia", 400);
        }

        LocalTime horaInicio = inicio.toLocalTime();
        LocalTime horaFin = fin.toLocalTime();

        if (horaInicio.isBefore(HORA_MINIMA)) {
            throw new NegocioException("La reserva no puede iniciar antes de las 06:00", 400);
        }
        if (horaFin.isAfter(HORA_MAXIMA)) {
            throw new NegocioException("La reserva no puede terminar despues de las 22:00", 400);
        }

        long minutosDuracion = Duration.between(inicio, fin).toMinutes();

        if (minutosDuracion < 60) {
            throw new NegocioException("La reserva debe durar al menos 1 hora", 400);
        }
        if (minutosDuracion > 240) {
            throw new NegocioException("La reserva no puede durar mas de 4 horas", 400);
        }

        Usuario instructor = reserva.getInstructor();
        if (instructor == null || instructor.getId() == null) {
            throw new NegocioException("Debe seleccionar un instructor para la reserva", 400);
        }

        Usuario instructorReal = usuarioRepository.findById(instructor.getId()).orElse(null);
        if (instructorReal == null) {
            throw new NegocioException("El instructor no existe en el sistema", 404);
        }

        if (instructorReal.getActivo() == false) {
            throw new NegocioException("El instructor no esta activo en el sistema", 400);
        }

        LocalDate diaReserva = inicio.toLocalDate();
        List<Reserva> reservasInstructor = reservaRepository.findByInstructorAndEstado(
                instructorReal, EstadoReserva.ACTIVA);

        int contadorDia = 0;
        for (int i = 0; i < reservasInstructor.size(); i++) {
            Reserva r = reservasInstructor.get(i);
            if (r.getFechaHoraInicio().toLocalDate().isEqual(diaReserva)) {
                contadorDia = contadorDia + 1;
            }
        }

        if (contadorDia >= 3) {
            throw new NegocioException("El instructor ya tiene 3 reservas activas el dia " + diaReserva, 400);
        }

        List<Reserva> solapadas = reservaRepository.findSolapadas(ambienteReal.getId(), inicio, fin);

        if (solapadas.size() > 0) {
            throw new NegocioException("El ambiente ya tiene una reserva activa en ese horario", 409);
        }

        reserva.setAmbiente(ambienteReal); 
        reserva.setInstructor(instructorReal);
        reserva.setEstado(EstadoReserva.ACTIVA);
        
        return reservaRepository.save(reserva);
    }

    public Reserva cancelarReserva(Long id) {

        Reserva reserva = reservaRepository.findById(id).orElse(null);

        if (reserva == null) {
            throw new NegocioException("No se encontro una reserva con id " + id, 404);
        }

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new NegocioException("La reserva ya esta cancelada", 400);
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(reserva.getFechaHoraInicio())) {
            throw new NegocioException("No se puede cancelar una reserva que ya inicio", 400);
        }

        long minutosFaltantes = Duration.between(ahora, reserva.getFechaHoraInicio()).toMinutes();

        if (minutosFaltantes < 120) {
            throw new NegocioException("Solo se puede cancelar con al menos 2 horas de anticipacion", 400);
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        Reserva reservaCancelada = reservaRepository.save(reserva);

        return reservaCancelada;
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> obtenerPorInstructor(String nombre) {
        return reservaRepository.findByInstructorNombreCompletoContainingIgnoreCase(nombre);
    }

    public Reserva obtenerPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new NegocioException("Reserva no encontrada con el ID: " + id, 404));
    }
}