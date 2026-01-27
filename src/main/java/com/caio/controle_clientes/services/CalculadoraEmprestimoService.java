package com.caio.controle_clientes.services;

import com.caio.controle_clientes.models.Emprestimo;
import com.caio.controle_clientes.models.ParcelaStatus;
import com.caio.controle_clientes.models.Parcelas;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculadoraEmprestimoService {
    public BigDecimal calcularParcela(BigDecimal valorEmprestado,
                                       BigDecimal taxaJuros,
                                       int quantidadeParcelas) {

        BigDecimal i = taxaJuros
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal umMaisI = BigDecimal.ONE.add(i);

        BigDecimal potencia = umMaisI.pow(quantidadeParcelas);

        BigDecimal potenciaNegativa = BigDecimal.ONE
                .divide(potencia, 10, RoundingMode.HALF_UP);

        BigDecimal denominador = BigDecimal.ONE.subtract(potenciaNegativa);

        BigDecimal numerador = valorEmprestado.multiply(i);

        return numerador.divide(denominador, 2, RoundingMode.HALF_UP);
    }

    public List<Parcelas> gerarParcelas(Emprestimo emprestimo) {
        List<Parcelas> parcelas = new ArrayList<>();

        BigDecimal saldoDevedor = emprestimo.getValorEmprestado();

        BigDecimal taxa = emprestimo.getTaxaDeJuros()
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal valorParcela = calcularParcela(
                emprestimo.getValorEmprestado(),
                emprestimo.getTaxaDeJuros(),
                emprestimo.getQuantidadeParcelas()
        );

        for (int numeroParcela = 1; numeroParcela <= emprestimo.getQuantidadeParcelas(); numeroParcela++) {
            BigDecimal jurosParcela = saldoDevedor.multiply(taxa);
            BigDecimal amortizacao = valorParcela.subtract(jurosParcela);

            saldoDevedor = saldoDevedor.subtract(amortizacao);

            Parcelas parcela = new Parcelas();
            parcela.setNumeroParcela(numeroParcela);
            parcela.setValorParcela(valorParcela.setScale(2, RoundingMode.HALF_UP));
            parcela.setDataVencimento(LocalDate.now().plusMonths(numeroParcela));
            parcela.setStatus(ParcelaStatus.PENDENTE);
            parcela.setEmprestimo(emprestimo);

            parcelas.add(parcela);
        }

        return parcelas;
    }
}
