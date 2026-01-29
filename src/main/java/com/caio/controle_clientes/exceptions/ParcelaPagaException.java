package com.caio.controle_clientes.exceptions;

public class ParcelaPagaException extends Exception {
    public ParcelaPagaException() {
        super("Parcela já está paga.");
    }
}
