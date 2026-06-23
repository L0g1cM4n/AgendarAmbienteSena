package com.sena.database_connection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Esta clase captura los errores de TODA la aplicacion, no solo de un controlador
// Asi no hay que repetir el try-catch en cada controlador
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura los errores de reglas de negocio que lanzamos con NegocioException
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> manejarNegocioException(NegocioException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getStatus()));
    }

    // Captura cualquier otro error inesperado del servidor
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepcionGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse("Error interno del servidor: " + ex.getMessage(), 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}