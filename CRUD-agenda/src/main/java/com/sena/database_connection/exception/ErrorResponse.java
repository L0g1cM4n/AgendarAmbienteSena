package com.sena.database_connection.exception;

import java.time.LocalDateTime;

// Esta clase define el formato JSON que se devuelve cuando hay un error
// En vez de devolver solo un String, devuelve un objeto con mas informacion
public class ErrorResponse {

    private String mensaje;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensaje, int status) {
        this.mensaje = mensaje;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}