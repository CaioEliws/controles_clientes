package com.caio.controle_clientes.dto;

import java.math.BigDecimal;

public record PagarParcelaDTO(
        Long idEmprestimo,
        Integer numeroParcela
) {}
