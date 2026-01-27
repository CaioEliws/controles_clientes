package com.caio.controle_clientes.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String nomeIndicador;

    private LocalDate criadoEm = LocalDate.now();

    @OneToMany(mappedBy = "cliente",
            cascade = CascadeType.ALL)
    private List<Emprestimo> emprestimos;

    public Cliente() {}

    public Cliente(String nome,
                   String nomeIndicador,
                   LocalDate criadoEm,
                   List<Emprestimo> emprestimos) {
        this.nome = nome;
        this.nomeIndicador = nomeIndicador;
        this.criadoEm = criadoEm;
        this.emprestimos = emprestimos;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeIndicador() {
        return nomeIndicador;
    }

    public void setNomeIndicador(String nomeIndicador) {
        this.nomeIndicador = nomeIndicador;
    }

    public LocalDate getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}
