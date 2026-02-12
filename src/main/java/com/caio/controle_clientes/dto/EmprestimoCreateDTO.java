package com.caio.controle_clientes.dto;

import com.caio.controle_clientes.models.FormaPagamento;

import java.math.BigDecimal;

public record EmprestimoCreateDTO(
        BigDecimal valorEmprestado,
        int quantidadeParcelas,
        BigDecimal jurosCobrado,
        FormaPagamento formaPagamento
) {}
