package isi.dan.msclientes.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.model.Cliente;

@SpringBootTest
public class ClienteServiceTest {
	
	@Autowired
	private ClienteService clienteService;
	
	@MockBean
	private ClienteRepository clienteRepository;
	
	private Cliente clienteUno, clienteDos;
	
	@BeforeEach
    void setUp() {
        clienteUno = new Cliente();
        clienteUno.setId(1);
        clienteUno.setNombre("cliente1");
        clienteUno.setCorreoElectronico("cliente1@mail.com");
        clienteUno.setMaximoDescubierto(BigDecimal.valueOf(15000));
        clienteUno.setCantObrasDisponibles(2);
        
        clienteDos = new Cliente();
        clienteDos.setId(2);
        clienteDos.setNombre("cliente2");
        clienteDos.setCorreoElectronico("cliente2@mail.com");
        clienteDos.setMaximoDescubierto(BigDecimal.valueOf(25000));
        clienteDos.setCantObrasDisponibles(0);
    }
	
	@Test
	void findAllTest() {
		List<Cliente> clientes = List.of(clienteUno, clienteDos);
		Mockito.when(clienteRepository.findAll()).thenReturn(clientes);
		assertEquals(clienteService.findAll(), clientes);
		verify(clienteRepository, times(1)).findAll();
	}

}
