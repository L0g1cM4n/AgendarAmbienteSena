package com.sena.database_connection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.sena.database_connection.model.entities.Ambiente;
import com.sena.database_connection.service.AmbienteService;

@RestController
@RequestMapping("/api/ambientes")
public class AmbienteController {

    @Autowired
    private AmbienteService ambienteService;

    @GetMapping
    public ResponseEntity<List<Ambiente>> listar() { 
        return ResponseEntity.ok(ambienteService.listarTodos()); 
    }

    @PostMapping
    public ResponseEntity<Ambiente> crear(@RequestBody Ambiente ambiente) {
        return new ResponseEntity<>(ambienteService.guardar(ambiente), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ambienteService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}