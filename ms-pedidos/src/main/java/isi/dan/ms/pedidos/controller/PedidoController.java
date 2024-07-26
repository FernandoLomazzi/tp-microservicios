package isi.dan.ms.pedidos.controller;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import isi.dan.ms.pedidos.Exceptions.ClienteNotFoundException;
import isi.dan.ms.pedidos.Exceptions.PedidoNotFoundException;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.servicio.PedidoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;

    Logger log = LoggerFactory.getLogger(PedidoController.class);


    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        Pedido savedPedido = pedidoService.savePedido(pedido);
        return ResponseEntity.ok(savedPedido);
    }

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable String id) throws PedidoNotFoundException {
        Pedido pedido = pedidoService.getPedidoById(id);
        if(pedido !=null){
           return ResponseEntity.ok(pedido);
        }else{
            throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable String id) throws PedidoNotFoundException {
        Pedido pedido = pedidoService.getPedidoById(id);
        if(pedido !=null){
            pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
         }else{
             throw new PedidoNotFoundException("Pedido '" + id + "' no encontrado");
         }
        
    }
}

