package com.caio.controle_clientes.services;

import com.caio.controle_clientes.models.Cliente;
import com.caio.controle_clientes.repository.ClienteRepositorio;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private ClienteRepositorio clienteRepositorio;

    public ClienteService(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    public Cliente buscarPorNome(String nome) {
        return clienteRepositorio
                .findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    public void criarCliente(String nome, String indicador) {
        boolean  existe = clienteRepositorio
                    .findByNomeContainingIgnoreCase(nome)
                    .isPresent();

        if (existe) {
            throw new RuntimeException("Cliente já existe!");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setNomeIndicador(indicador);

        clienteRepositorio.save(cliente);
    }
}
