package isi.dan.ms.pedidos.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.pedidos.Exceptions.ClienteNotFoundException;
import isi.dan.ms.pedidos.Exceptions.IlegalStateException;
import isi.dan.ms.pedidos.Exceptions.PedidoNotFoundException;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.servicio.PedidoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	Logger log = LoggerFactory.getLogger(PedidoController.class);

	@PostMapping
	public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
		Pedido savedPedido = pedidoService.savePedido(pedido);
		log.debug("Pedido creado!!");
		return ResponseEntity.ok(savedPedido);
	}

	@GetMapping
	public List<Pedido> getAllPedidos() {
		return pedidoService.getAllPedidos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Pedido> getPedidoById(@PathVariable String id) throws PedidoNotFoundException {
		log.info("Entrando al método getPedidoById con id: " + id);
		Pedido pedido = pedidoService.getPedidoById(id);
		if (pedido != null) {
			return ResponseEntity.ok(pedido);
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}
	}

	@GetMapping("/nroPedido/{id}")
	public ResponseEntity<Pedido> getPedidoBynro(@PathVariable String id) throws PedidoNotFoundException {
		log.info("Entrando al método getPedidoByNroPedido con id: " + id);

		Pedido pedido = pedidoService.getPedidoByNroPedido(id);
		if (pedido != null) {
			return ResponseEntity.ok(pedido);
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

	@GetMapping("/nroCliente/{id}")
	public ResponseEntity<List<Pedido>> getPedidoByCliente(@PathVariable String id) throws ClienteNotFoundException {
		log.info("Entrando al método getPedidoByCliente con id: " + id);

		List<Pedido> pedidos = pedidoService.getAllPedidoByCliente(id);
		log.info("recuperé pedidos de cliente");

		if (!pedidos.isEmpty()) {
			log.info(pedidos.getFirst().getId());
			return ResponseEntity.ok(pedidos);
		} else {
			throw new ClienteNotFoundException("no se encuentran pedidos para el cliente con id: '" + id + "'");
		}

	}

	@DeleteMapping("/nroPedido/{id}")
	public ResponseEntity<Pedido> DeletePedidoBynro(@PathVariable String id) throws PedidoNotFoundException {
		log.info("Entrando al método DeletePedidoBynro con id: " + id);

		Pedido pedido = pedidoService.getPedidoByNroPedido(id);
		if (pedido != null) {

			pedidoService.deletePedido(pedido.getId());
			log.debug("Pedido eliminado!!");
			return ResponseEntity.noContent().build();
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePedido(@PathVariable String id) throws PedidoNotFoundException {
		Pedido pedido = pedidoService.getPedidoById(id);
		if (pedido != null) {
			pedidoService.deletePedido(id);
			log.debug("Pedido eliminado!!");
			return ResponseEntity.noContent().build();
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<Pedido> update(@PathVariable String id, @RequestBody Pedido pedidoUpdatear)
			throws PedidoNotFoundException {
		Pedido pedido = pedidoService.getPedidoById(id);
		if (pedido != null) {
			log.debug("Pedido actualizado!!");
			return ResponseEntity.ok(pedidoService.update(pedidoUpdatear, id));
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}
	}

	@PutMapping("/nroPedido/{id}")
	public ResponseEntity<Pedido> updateByNroPedido(@PathVariable String id, @RequestBody Pedido pedidoUpdatear)
			throws PedidoNotFoundException {
		Pedido pedido = pedidoService.getPedidoByNroPedido(id);
		if (pedido != null) {
			log.debug("Pedido actualizado!!");
			return ResponseEntity.ok(pedidoService.update(pedidoUpdatear, pedido.getId()));
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<Pedido> update(@PathVariable String id, @PathVariable String estado)
			throws PedidoNotFoundException, IlegalStateException {
		Pedido pedido = pedidoService.getPedidoById(id);
		if (pedido != null) {

			try {
				EstadoPedido estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
				if (estadoPedido == EstadoPedido.CANCELADO) {
//hay que handlear en caso de que se cancele la devolucion del stock
					pedidoService.restockProducts(pedido);
				}
				pedido.updateState(estadoPedido, pedido.getUsuario(), null);
				log.debug("Pedido actualizado!!");
				return ResponseEntity.ok(pedidoService.update(pedido, id));
			} catch (IllegalArgumentException ex) {
				throw new IlegalStateException("estado: " + estado + " no es un estado valido");
			}
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

	@PutMapping("/nroPedido/{id}/estado/{estado}")
	public ResponseEntity<Pedido> updateByNroPedidoAndState(@PathVariable String id, @PathVariable String estado)
			throws PedidoNotFoundException, IlegalStateException {
		Pedido pedido = pedidoService.getPedidoByNroPedido(id);
		if (pedido != null) {

			try {
				EstadoPedido estadoPedido = EstadoPedido.valueOf(estado.toUpperCase());
				if (estadoPedido == EstadoPedido.CANCELADO) {
//hay que handlear en caso de que se cancele la devolucion del stock
					pedidoService.restockProducts(pedido);
				}
				pedido.updateState(estadoPedido, pedido.getUsuario(), null);
				log.debug("Pedido actualizado!!");
				return ResponseEntity.ok(pedidoService.update(pedido, pedido.getId()));
			} catch (IllegalArgumentException ex) {
				throw new IlegalStateException("estado: " + estado + " no es un estado valido");
			}
		} else {
			throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
		}

	}

}
