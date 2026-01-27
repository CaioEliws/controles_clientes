package com.caio.controle_clientes.services;

import com.caio.controle_clientes.models.FormaPagamento;

import java.util.Scanner;

public class PagamentoMenuService {
    private static final Scanner input = new Scanner(System.in);

    public static FormaPagamento escolherFormaPagamento() {
        while (true) {
            System.out.println("Forma de pagamento:\n1 - PIX\n2 - DINHEIRO");
            int opcao = input.nextInt();
            input.nextLine();

            switch (opcao) {
                case 1 -> { return FormaPagamento.PIX; }
                case 2 -> { return FormaPagamento.DINHEIRO; }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
