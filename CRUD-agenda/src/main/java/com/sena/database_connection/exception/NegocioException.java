package com.sena.database_connection.exception;

public class NegocioException extends RuntimeException {

    private int status;

    public NegocioException(String mensaje, int status) {
        super(mensaje);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

