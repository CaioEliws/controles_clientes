package com.caio.controle_clientes.services;

import com.caio.controle_clientes.models.Cliente;
import com.caio.controle_clientes.repository.ClienteRepositorio;
import com.caio.controle_clientes.repository.EmprestimoRepositorio;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    private ClienteRepositorio clienteRepositorio;
    private EmprestimoRepositorio emprestimoRepositorio;

    public ClienteService(ClienteRepositorio clienteRepositorio,
                          EmprestimoRepositorio emprestimoRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.emprestimoRepositorio = emprestimoRepositorio;
    }

    public Cliente getClienteById(Long id) {
        return clienteRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
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

    public void updateNameCliente(Long id, String nome) {
        Cliente cliente = getClienteById(id);
        cliente.setNome(nome);
        clienteRepositorio.save(cliente);
    }

    public void updateEnderecoCliente(Long id, String rua, String bairro, Integer numero) {
        Cliente cliente = getClienteById(id);

        cliente.setEnderecoRua(rua);
        cliente.setEnderecoBairro(bairro);
        cliente.setEnderecoNumero(numero);

        clienteRepositorio.save(cliente);
    }

    public void deletarCliente(Long clienteId) {
        Cliente cliente = getClienteById(clienteId);

        boolean possuiEmprestimos =
                emprestimoRepositorio.existsByClienteId(clienteId);

        if (possuiEmprestimos) {
            throw new RuntimeException(
                    "Não é possível deletar o cliente pois ele possui empréstimos cadastrados."
            );
        }

        clienteRepositorio.delete(cliente);
    }
}
