package isi.dan.msclientes.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.model.Cliente;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Value("${ms-clientes.max-cant-obras-disponibles:1}")
	private Integer cantObrasDisponibles;

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> findById(Integer id) {
		return clienteRepository.findById(id);
	}

	public Cliente save(Cliente cliente) {
		cliente.setCantObrasDisponibles(cantObrasDisponibles);
		return clienteRepository.save(cliente);
	}

	public Cliente update(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public void deleteById(Integer id) {
		clienteRepository.deleteById(id);
	}

}
