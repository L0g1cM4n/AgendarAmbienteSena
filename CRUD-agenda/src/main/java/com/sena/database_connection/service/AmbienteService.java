package com.sena.database_connection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.http.HttpStatus;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.repositories.AmbienteRepository;
import com.sena.database_connection.exception.NegocioException;

@Service
public class AmbienteService {

    @Autowired
    private AmbienteRepository ambienteRepository;

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
}