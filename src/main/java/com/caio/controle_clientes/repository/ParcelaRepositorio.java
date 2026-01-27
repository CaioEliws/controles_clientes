package com.caio.controle_clientes.repository;

import com.caio.controle_clientes.models.Parcelas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParcelaRepositorio extends JpaRepository<Parcelas, Long> {
    Optional<Parcelas> findById(Long id);

    Optional<Object> findByEmprestimoIdAndNumeroParcela(Long idEmprestimo, Integer numeroParcela);
}
