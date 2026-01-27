package com.caio.controle_clientes.principal;

import com.caio.controle_clientes.models.Cliente;
import com.caio.controle_clientes.models.Emprestimo;
import com.caio.controle_clientes.models.FormaPagamento;
import com.caio.controle_clientes.models.Parcelas;
import com.caio.controle_clientes.repository.ClienteRepositorio;
import com.caio.controle_clientes.repository.EmprestimoRepositorio;
import com.caio.controle_clientes.repository.ParcelaRepositorio;
import com.caio.controle_clientes.services.ClienteService;
import com.caio.controle_clientes.services.EmprestimoService;
import com.caio.controle_clientes.services.PagamentoMenuService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    Scanner input = new Scanner(System.in);

    private EmprestimoService emprestimoService;
    private ClienteService clienteService;

    public Principal(EmprestimoService emprestimoService,
                     ClienteService clienteService) {
        this.emprestimoService = emprestimoService;
        this.clienteService = clienteService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            String menu = """
                    1 - Cadastrar cliente
                    2 - Criar empréstimo
                    3 - Pagar parcela
                    0 - Sair
                    """;

            System.out.println(menu);
            option = input.nextInt();
            input.nextLine();

            switch (option) {
                case 1 -> criarCliente();
                case 2 -> criarEmprestimo();
                case 3 -> pagarParcela();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        }
    }

    private Cliente buscarClienteExistente() {
        while (true) {
            System.out.print("Digite o nome do cliente: ");
            String nome = input.nextLine();

            try {
                return clienteService.buscarPorNome(nome);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.out.println("Cliente não encontrado. Tente novamente.\n");
            }
        }
    }

    private void criarCliente() {
        System.out.println("Digite o nome do cliente:");
        String nomeCliente = input.nextLine();

        System.out.println("Nome do indicador: ");
        var nomeIndicador = input.nextLine();

        clienteService.criarCliente(nomeCliente, nomeIndicador);

        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void criarEmprestimo() {
        Cliente cliente = buscarClienteExistente();

        FormaPagamento formaPagamento = PagamentoMenuService.escolherFormaPagamento();

        System.out.println("Valor emprestado: ");
        BigDecimal valorEmprestado = input.nextBigDecimal();

        System.out.println("Quantidade de parcelas:");
        int quantidadeParcelas = input.nextInt();

        System.out.println("Juros cobrado (%): ");
        BigDecimal jurosCobrado = input.nextBigDecimal();
        input.nextLine();

        emprestimoService.criarEmprestimo(
                cliente,
                valorEmprestado,
                quantidadeParcelas,
                jurosCobrado,
                formaPagamento
        );

        System.out.println("Emprèstimo criado com sucesso!");
    }

    private void pagarParcela() {
        Cliente cliente = buscarClienteExistente();

        List<Emprestimo> emprestimos =
                emprestimoService.listarEmprestimosCliente(cliente);

        if (emprestimos.isEmpty()) {
            System.out.println("Esse cliente não possui empréstimos.");
            return;
        }

        System.out.println("\nEmpréstimos:");

        emprestimos.forEach(e -> {
            System.out.println(
                    "ID: " + e.getId() +
                            " | Total: " + e.getValorTotalEmprestimo() +
                            " | A Receber: " + e.getValorAReceber() +
                            " | Status: " + e.getEmprestimoStatus()
            );
        });

        System.out.print("\nDigite o ID do empréstimo: ");
        Long idEmprestimo = input.nextLong();
        input.nextLine();

        List<Parcelas> parcelas = emprestimoService.listarParcelas(idEmprestimo);

        System.out.println("\nParcelas:");

        parcelas.forEach(p -> {
            System.out.println(
                    "Parcela: " + p.getNumeroParcela() +
                            " | Valor: " + p.getValorParcela() +
                            " | Status: " + p.getStatus()
            );
        });

        System.out.print("\nDigite o número da parcela: ");
        Integer numeroParcela = input.nextInt();
        input.nextLine();

        emprestimoService.pagarParcela(idEmprestimo, numeroParcela);

        System.out.println("\nParcela paga com sucesso!");
    }
}
