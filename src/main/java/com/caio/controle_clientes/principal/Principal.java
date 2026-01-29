package com.caio.controle_clientes.principal;

import com.caio.controle_clientes.exceptions.EmprestimoQuitadoException;
import com.caio.controle_clientes.exceptions.ParcelaPagaException;
import com.caio.controle_clientes.models.*;
import com.caio.controle_clientes.repository.ClienteRepositorio;
import com.caio.controle_clientes.repository.EmprestimoRepositorio;
import com.caio.controle_clientes.repository.ParcelaRepositorio;
import com.caio.controle_clientes.services.ClienteService;
import com.caio.controle_clientes.services.EmprestimoService;
import com.caio.controle_clientes.services.PagamentoMenuService;
import com.caio.controle_clientes.services.ParcelaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    Scanner input = new Scanner(System.in);

    private ParcelaRepositorio parcelaRepositorio;

    private EmprestimoService emprestimoService;
    private ClienteService clienteService;
    private ParcelaService parcelaService;

    public Principal(EmprestimoService emprestimoService,
                     ClienteService clienteService,
                     ParcelaRepositorio parcelaRepositorio,
                     ParcelaService parcelaService) {
        this.emprestimoService = emprestimoService;
        this.clienteService = clienteService;
        this.parcelaRepositorio = parcelaRepositorio;
        this.parcelaService = parcelaService;
    }

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            String menu = """
                    1 - Cadastrar cliente
                    2 - Criar empréstimo
                    3 - Pagar parcela
                    4 - Listar todas as parcelas pendentes
                    5 - Listar todas parcelas atrasadas
                    6 - Listar parcelas vencem hoje
                    7 - Listar empréstimos de um cliente
                    0 - Sair
                    """;

            System.out.println(menu);
            option = input.nextInt();
            input.nextLine();

            switch (option) {
                case 1 -> criarCliente();
                case 2 -> criarEmprestimo();
                case 3 -> pagarParcela();
                case 4 -> listarParcelasPorStatus(ParcelaStatus.PENDENTE);
                case 5 -> listarParcelasPorStatus(ParcelaStatus.ATRASADO);
                case 6 -> listarParcelasVencendoHoje(ParcelaStatus.PENDENTE, LocalDate.now());
                case 7 -> listarEmprestimos();
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

        emprestimos.forEach(e -> {
            System.out.println(
                    "\n----------------------------" +
                            "\nID: " + e.getId() +
                            "\nTotal: " + e.getValorTotalEmprestimo() +
                            "\nA Receber: " + e.getValorAReceber() +
                            "\nStatus: " + e.getEmprestimoStatus() +
                            "\n----------------------------"
            );
        });

        System.out.print("\nDigite o ID do empréstimo: ");
        Long idEmprestimo = input.nextLong();
        input.nextLine();

        Emprestimo emprestimoSelecionado = emprestimos.stream()
                .filter(e -> e.getId().equals(idEmprestimo))
                .findFirst()
                .orElse(null);

        if (emprestimoSelecionado == null) {
            System.out.println("Empréstimo não encontrado.");
            return;
        }

        if (emprestimoSelecionado.getEmprestimoStatus() == EmprestimoStatus.QUITADO) {
            System.out.println("Esse empréstimo já está quitado.");
            return;
        }

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

        try {
            emprestimoService.pagarParcela(idEmprestimo, numeroParcela);
            System.out.println("\nParcela paga com sucesso!");
        } catch (EmprestimoQuitadoException e) {
            System.out.println(e.getMessage());
        } catch (ParcelaPagaException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Erro: " + e.getMessage());

        }
    }

    private void listarParcelasPorStatus(ParcelaStatus status) {
        parcelaService.atualizarParcelasAtrasadas();

        List<Parcelas> parcelas = parcelaRepositorio.findParcelasComEmprestimoEClientePorStatus(status);

        if (parcelas.isEmpty()) {
            System.out.printf("Não há parcelas %s.%n", status.name().toLowerCase());
            return;
        }

        parcelas.forEach(p -> System.out.printf(
                "Cliente: %s | Parcela %d | Valor emprestado: R$%.2f | Valor parcela: R$%.2f | Data de vencimento: %s%n",
                p.getEmprestimo().getCliente().getNome(),
                p.getNumeroParcela(),
                p.getEmprestimo().getValorEmprestado(),
                p.getValorParcela(),
                p.getDataVencimento()
        ));
    }

    private void listarParcelasVencendoHoje(ParcelaStatus status, LocalDate dataVencimento) {
        parcelaService.atualizarParcelasAtrasadas();


        List<Parcelas> parcelas = parcelaRepositorio.findParcelasVencendoHoje(status, dataVencimento);

        if (parcelas.isEmpty()) {
            System.out.printf("Não há parcelas vencndo hoje.");
            return;
        }

        parcelas.forEach(p -> System.out.printf(
                "Cliente: %s | Parcela %d | Valor emprestado: R$%.2f | Valor parcela: R$%.2f | Data de vencimento: %s%n",
                p.getEmprestimo().getCliente().getNome(),
                p.getNumeroParcela(),
                p.getEmprestimo().getValorEmprestado(),
                p.getValorParcela(),
                p.getDataVencimento()
        ));
    }

    private void listarEmprestimos() {

        Cliente cliente = buscarClienteExistente();

        List<Emprestimo> emprestimos =
                emprestimoService.listarEmprestimosCliente(cliente);

        if (emprestimos.isEmpty()) {
            System.out.println("Esse cliente não possui empréstimos.");
            return;
        }

        System.out.println("\nEmpréstimos:");

        emprestimos.forEach(e -> {
            System.out.printf(
                    "ID: %d | Total: %s | A Receber: %s | Status: %s%n",
                    e.getId(),
                    e.getValorTotalEmprestimo(),
                    e.getValorAReceber(),
                    e.getEmprestimoStatus()
            );
        });

        System.out.print("\nDigite o ID do empréstimo para ver parcelas: ");
        Long idEmprestimo = input.nextLong();
        input.nextLine();

        Emprestimo emprestimoSelecionado = emprestimos.stream()
                .filter(e -> e.getId().equals(idEmprestimo))
                .findFirst()
                .orElse(null);

        if (emprestimoSelecionado == null) {
            System.out.println("Empréstimo não encontrado.");
            return;
        }

        List<Parcelas> parcelas =
                emprestimoService.listarParcelas(idEmprestimo);

        System.out.println("\nParcelas:");

        parcelas.forEach(p -> {
            System.out.printf(
                    "Parcela %d | Valor: %s | Status: %s | Vencimento: %s%n",
                    p.getNumeroParcela(),
                    p.getValorParcela(),
                    p.getStatus(),
                    p.getDataVencimento()
            );
        });
    }
}
