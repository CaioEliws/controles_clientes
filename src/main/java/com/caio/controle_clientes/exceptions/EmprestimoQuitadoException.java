package com.caio.controle_clientes.exceptions;

public class EmprestimoQuitadoException extends RuntimeException {
    public EmprestimoQuitadoException() {
        super("Empréstimo já está quitado.");
    }
}
