package com.caio.controle_clientes.repository;

import com.caio.controle_clientes.models.ParcelaStatus;
import com.caio.controle_clientes.models.Parcelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParcelaRepositorio extends JpaRepository<Parcelas, Long> {
    Optional<Parcelas> findById(Long id);

    Optional<Object> findByEmprestimoIdAndNumeroParcela(Long idEmprestimo, Integer numeroParcela);

    @Query("SELECT p FROM Parcelas p JOIN FETCH p.emprestimo e JOIN FETCH e.cliente WHERE p.status = :status ORDER BY p.dataVencimento ASC")
    List<Parcelas> findParcelasComEmprestimoEClientePorStatus(@Param("status") ParcelaStatus pendente);

    List<Parcelas> findByStatusAndDataVencimentoBefore(ParcelaStatus parcelaStatus, LocalDate now);

    @Query("SELECT p FROM Parcelas p JOIN FETCH p.emprestimo e JOIN FETCH e.cliente c WHERE p.status = :status AND p.dataVencimento = :data")
    List<Parcelas> findParcelasVencendoHoje(@Param("status") ParcelaStatus status,
                                            @Param("data") LocalDate dataVencimento);
}
