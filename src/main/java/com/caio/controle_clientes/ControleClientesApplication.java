package com.caio.controle_clientes;

import com.caio.controle_clientes.principal.Principal;
import com.caio.controle_clientes.repository.ClienteRepositorio;
import com.caio.controle_clientes.repository.EmprestimoRepositorio;
import com.caio.controle_clientes.repository.ParcelaRepositorio;
import com.caio.controle_clientes.services.CalculadoraEmprestimoService;
import com.caio.controle_clientes.services.ClienteService;
import com.caio.controle_clientes.services.EmprestimoService;
import com.caio.controle_clientes.services.ParcelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControleClientesApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ControleClientesApplication.class, args);
	}

    @Autowired
    private EmprestimoService emprestimoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ParcelaRepositorio parcelaRepositorio;
    @Autowired
    private ParcelaService parcelaService;

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(
                emprestimoService,
                clienteService,
                parcelaRepositorio,
                parcelaService
        );
        principal.showMenu();
    }
}
