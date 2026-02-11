package com.caio.controle_clientes.controllers;

import com.caio.controle_clientes.dto.UpdateEnderecoDTO;
import com.caio.controle_clientes.dto.UpdateNomeClienteDTO;
import com.caio.controle_clientes.services.ClienteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    private final ClienteService clienteService;

    public ClientController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PatchMapping("/{id}/name")
    public void updateName(@PathVariable Long id, @RequestBody UpdateNomeClienteDTO dto) {
        clienteService.updateNameCliente(id, dto.getNome());
    }

    @PatchMapping("/{id}/endereco")
    public void updateEndereco(@PathVariable Long id, @RequestBody UpdateEnderecoDTO dto) {
        clienteService.updateEnderecoCliente(
                id,
                dto.getRua(),
                dto.getBairro(),
                dto.getNumero()
        );
    }
}
