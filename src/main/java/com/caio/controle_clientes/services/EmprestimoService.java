package com.caio.controle_clientes.services;

import com.caio.controle_clientes.exceptions.EmprestimoQuitadoException;
import com.caio.controle_clientes.exceptions.ParcelaPagaException;
import com.caio.controle_clientes.models.*;
import com.caio.controle_clientes.repository.EmprestimoRepositorio;
import com.caio.controle_clientes.repository.ParcelaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {
    private EmprestimoRepositorio emprestimoRepositorio;
    private ParcelaRepositorio parcelaRepositorio;
    private CalculadoraEmprestimoService calculadora;

    public EmprestimoService(EmprestimoRepositorio emprestimoRepositorio,
                             ParcelaRepositorio parcelaRepositorio,
                             CalculadoraEmprestimoService calculadora) {
        this.emprestimoRepositorio = emprestimoRepositorio;
        this.parcelaRepositorio = parcelaRepositorio;
        this.calculadora = calculadora;
    }

    @Transactional
    public Emprestimo criarEmprestimo(Cliente cliente,
                                      BigDecimal valorEmprestado,
                                      int quantidadeParcelas,
                                      BigDecimal jurosCobrado,
                                      FormaPagamento formaPagamento) {
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setCliente(cliente);
        emprestimo.setValorEmprestado(valorEmprestado);
        emprestimo.setQuantidadeParcelas(quantidadeParcelas);
        emprestimo.setTaxaDeJuros(jurosCobrado);
        emprestimo.setFormaPagamento(formaPagamento);

        emprestimo.setDataDoEmprestimo(LocalDate.now());
        emprestimo.setInicioPagamento(LocalDate.now().plusMonths(1));
        emprestimo.setFinalPagamento(LocalDate.now().plusMonths(quantidadeParcelas));

        emprestimo.setEmprestimoStatus(EmprestimoStatus.EM_ABERTO);

        BigDecimal valorParcela =
                calculadora.calcularParcela(valorEmprestado, jurosCobrado, quantidadeParcelas);

        BigDecimal valorTotal = valorParcela
                .multiply(BigDecimal.valueOf(quantidadeParcelas))
                .setScale(2, RoundingMode.HALF_UP);

        emprestimo.setValorParcela(valorParcela);

        emprestimo.setValorTotalEmprestimo(valorTotal);
        emprestimo.setValorAReceber(valorTotal);
        emprestimo.setValorRecebido(BigDecimal.ZERO);

        emprestimoRepositorio.save(emprestimo);

        List<Parcelas> parcelas = calculadora.gerarParcelas(emprestimo);

        emprestimo.setParcelas(parcelas);

        parcelaRepositorio.saveAll(parcelas);

        return emprestimo;
    }

    @Transactional
    public void pagarParcela(Long idEmprestimo, Integer numeroParcela)
            throws ParcelaPagaException, EmprestimoQuitadoException {

        Parcelas parcela = (Parcelas) parcelaRepositorio
                .findByEmprestimoIdAndNumeroParcela(idEmprestimo, numeroParcela)
                .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));

        Emprestimo emprestimo = parcela.getEmprestimo();

        if (emprestimo.getEmprestimoStatus() == EmprestimoStatus.QUITADO) {
            throw new EmprestimoQuitadoException();
        }

        if (parcela.getStatus() == ParcelaStatus.PAGO) {
            throw new ParcelaPagaException();
        }

        parcela.setStatus(ParcelaStatus.PAGO);
        parcela.setDataPagamento(LocalDate.now());

        BigDecimal valor = parcela.getValorParcela();

        emprestimo.setValorRecebido(
                emprestimo.getValorRecebido().add(valor)
        );

        emprestimo.setValorAReceber(
                emprestimo.getValorAReceber().subtract(valor)
        );

        if (emprestimo.getValorAReceber().compareTo(BigDecimal.ZERO) <= 0) {
            emprestimo.setEmprestimoStatus(EmprestimoStatus.QUITADO);
        }

        parcelaRepositorio.save(parcela);
        emprestimoRepositorio.save(emprestimo);
    }

    @Transactional
    public List<Emprestimo> listarEmprestimosCliente(Cliente cliente) {
        return emprestimoRepositorio.findAllByCliente(cliente);
    }

    @Transactional
    public List<Parcelas> listarParcelas(Long idEmprestimo) {
        Emprestimo emprestimo = emprestimoRepositorio.buscarComParcelas((idEmprestimo))
                .orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));

        return emprestimo.getParcelas();
    }
}
