package com.caio.controle_clientes.controllers;

import com.caio.controle_clientes.dto.InadimplenteDTO;
import com.caio.controle_clientes.models.Cliente;
import com.caio.controle_clientes.services.ClienteService;
import com.caio.controle_clientes.services.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {
    private final RelatorioService relatorioService;
    private final ClienteService clienteService;

    public RelatorioController(RelatorioService relatorioService,
                               ClienteService clienteService) {
        this.relatorioService = relatorioService;
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes/{idCliente}")
    public ResponseEntity<?> historicoCliente(
            @PathVariable Long idCliente
    ) {
        Cliente cliente = clienteService.getClienteById(idCliente);
        return ResponseEntity.ok(
                relatorioService.gerarHistoricoCliente(cliente)
        );
    }

    @GetMapping("/inadimplentes")
    public List<InadimplenteDTO> inadimplentes() {
        return relatorioService.gerarRelatorioInadimplentes();
    }
}

