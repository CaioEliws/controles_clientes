package com.caio.controle_clientes.controllers;

import com.caio.controle_clientes.dto.ClienteCreateDTO;
import com.caio.controle_clientes.dto.UpdateEnderecoDTO;
import com.caio.controle_clientes.dto.UpdateNomeClienteDTO;
import com.caio.controle_clientes.models.Cliente;
import com.caio.controle_clientes.services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    private final ClienteService clienteService;

    public ClientController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Void> criarCliente(@RequestBody ClienteCreateDTO dto) {
        clienteService.criarCliente(dto.nome(), dto.nomeIndicador(), dto.enderecoRua(), dto.enderecoBairro(), dto.enderecoNumero());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> updateName(
            @PathVariable Long id,
            @RequestBody UpdateNomeClienteDTO dto
    ) {
        clienteService.updateNameCliente(id, dto.getNome());
        return ResponseEntity.noContent().build();
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

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

}
