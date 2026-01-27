package com.caio.controle_clientes.repository;

import com.caio.controle_clientes.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNomeContainingIgnoreCase(String nome);
}
