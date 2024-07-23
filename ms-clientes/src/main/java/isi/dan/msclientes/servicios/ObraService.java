package isi.dan.msclientes.servicios;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoObra;
import isi.dan.msclientes.model.Obra;

@Service
public class ObraService {

	@Autowired
	private ObraRepository obraRepository;
	@Autowired
	private ClienteService clienteService;

	public List<Obra> findAll() {
		return obraRepository.findAll();
	}

	public Optional<Obra> findById(Integer id) {
		return obraRepository.findById(id);
	}

	public Obra save(Obra obra) {
		try {
			Cliente cliente = clienteService.findById(obra.getCliente().getId()).orElseThrow();
			obra.setCliente(cliente);
			cliente.tomarObra();
			obra.setEstado(EstadoObra.HABILITADA);
		} catch (NoSuchElementException e) {
			throw e; // hacer otra cosa
		} catch (Exception e) {
			obra.setEstado(EstadoObra.PENDIENTE);
		}
		return obraRepository.save(obra);
	}

	public Obra update(Obra obra) {
		return obraRepository.save(obra);
	}

	public void deleteById(Integer id) {
		obraRepository.deleteById(id);
	}

	public Obra habilitar(Obra obra) throws Exception {
		if (obra.getEstado().equals(EstadoObra.FINALIZADA))
			throw new Exception("No se puede habilitar una obra finalizada");
		if (obra.getEstado().equals(EstadoObra.HABILITADA))
			throw new Exception("La obra ya se encuentra habilitada");
		obra.getCliente().tomarObra();
		obra.setEstado(EstadoObra.HABILITADA);
		return this.update(obra);
	}

	public Obra deshabilitar(Obra obra) throws Exception {
		if (obra.getEstado().equals(EstadoObra.FINALIZADA))
			throw new Exception("No se puede deshabilitar una obra finalizada");
		if (obra.getEstado().equals(EstadoObra.PENDIENTE))
			throw new Exception("La obra ya se encuentra pendiente");
		obra.getCliente().liberarObra();
		obra.setEstado(EstadoObra.PENDIENTE);
		return this.update(obra);
	}

	public Obra finalizar(Obra obra) throws Exception {
		if (obra.getEstado().equals(EstadoObra.FINALIZADA))
			throw new Exception("La obra ya se encuentra finalizada");
		if (obra.getEstado().equals(EstadoObra.PENDIENTE))
			throw new Exception("La obra debe estar habilitada para ser finalizada");
		obra.getCliente().liberarObra();
		obra.setEstado(EstadoObra.FINALIZADA);
		List<Obra> obras = obtenerObras(obra.getCliente().getId(), EstadoObra.PENDIENTE);
		Integer obrasDisp = obra.getCliente().getCantObrasDisponibles();
		for (int i = 0; i < obrasDisp && i < obras.size(); i++) {
			habilitar(obras.get(i));
		}
		return this.update(obra);
	}

	private List<Obra> obtenerObras(Integer idCliente, EstadoObra estado) {
		if (estado == null)
			return obraRepository.findByClienteId(idCliente);
		return obraRepository.findByClienteIdAndEstadoEquals(idCliente, estado);
	}

}
