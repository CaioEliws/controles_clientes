package com.caio.controle_clientes.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parcelas")
public class Parcelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroParcela;

    private BigDecimal valorParcela;

    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private ParcelaStatus status = ParcelaStatus.PENDENTE;

    private LocalDate dataPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emprestimo_id")
    private Emprestimo emprestimo;

    @Column(nullable = false)
    private BigDecimal valorPago = BigDecimal.ZERO;

    public  Parcelas() {}

    public Parcelas(Integer numeroParcela,
                    BigDecimal valorParcela,
                    LocalDate dataVencimento,
                    ParcelaStatus status,
                    LocalDate dataPagamento,
                    Emprestimo emprestimo,
                    BigDecimal valorPago) {
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.dataPagamento = dataPagamento;
        this.emprestimo = emprestimo;
        this.valorPago = valorPago;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public ParcelaStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelaStatus status) {
        this.status = status;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }
}
