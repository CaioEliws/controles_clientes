package com.caio.controle_clientes.models;

public enum EmprestimoStatus {
    EM_ABERTO("Em aberto"),
    QUITADO("Quitado"),
    REFINANCIADO("Refinanciado"),
    QUITADO_TERCEIRO("Quitado por terceiro");

    private final String descricao;

    EmprestimoStatus(String description) {
        this.descricao = description;
    }

    public String getDescription() {
        return descricao;
    }
}
