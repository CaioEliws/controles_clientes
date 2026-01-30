package com.caio.controle_clientes.exceptions;

public class ParcelaPagaException extends RuntimeException {
    public ParcelaPagaException() {
        super("Parcela já está paga.");
    }
}
