package com.caio.controle_clientes.services;

import com.caio.controle_clientes.models.ParcelaStatus;
import com.caio.controle_clientes.models.Parcelas;
import com.caio.controle_clientes.repository.ParcelaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ParcelaService {
    @Autowired
    private ParcelaRepositorio parcelaRepositorio;

    @Transactional
    public void atualizarParcelasAtrasadas() {
        List<Parcelas> atrasadas = parcelaRepositorio.findByStatusAndDataVencimentoBefore(
                ParcelaStatus.PENDENTE, LocalDate.now()
        );

        atrasadas
                .forEach(p -> p.setStatus(ParcelaStatus.ATRASADO));
        parcelaRepositorio.saveAll(atrasadas);
    }
}
