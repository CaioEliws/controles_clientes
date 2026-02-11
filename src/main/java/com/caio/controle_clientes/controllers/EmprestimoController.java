package com.caio.controle_clientes.controllers;

import com.caio.controle_clientes.services.EmprestimoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes/{idCliente}/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @DeleteMapping("/{idEmprestimo}")
    public void deletarEmprestimo(
            @PathVariable Long idCliente,
            @PathVariable Long idEmprestimo
    ) {
        emprestimoService.deletarEmprestimo(idCliente, idEmprestimo);
    }
}