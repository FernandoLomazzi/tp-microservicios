package isi.dan.ms.pedidos.servicio;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import isi.dan.ms.pedidos.conf.RabbitMQConfig;
import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger log = LoggerFactory.getLogger(PedidoService.class);

    public Pedido savePedido(Pedido pedido) {
        // id se pone automatico por mongo
        //nro de pedido
        pedido.setNumeroPedido(this.getMaxNumeroPedido()+1);
        // colocar fecha
        pedido.setFecha(Instant.now());

        // calcular total
        BigDecimal totalDetalles = BigDecimal.ZERO;
        for (DetallePedido dp : pedido.getDetalle()) {
            totalDetalles.add(dp.getPrecioFinal());
        }
        pedido.setTotal(totalDetalles);
        RestTemplate restTemplate = new RestTemplate();
        String gatewayURL = "http://ms-gateway-svc:8080";

        // verificar saldo
        Cliente resultado = restTemplate
                .getForObject(gatewayURL + "/clientes/api/clientes/" + pedido.getCliente().getId(), Cliente.class);
        pedido.setCliente(resultado);
        BigDecimal saldo = BigDecimal.ZERO;
        // costos de pedidos previos
        for (Pedido p : this.getAllPedidos()) {
            if (p.getCliente().getId() == resultado.getId()
                    && (p.getEstado() == EstadoPedido.ACEPTADO || p.getEstado() == EstadoPedido.EN_PREPARACION))
                saldo = saldo.add(p.getTotal());

        }
        // costo del pedido actual
        saldo = saldo.add(pedido.getTotal());

        if (saldo.compareTo(resultado.getMaximoDescubierto()) > 0) {
            pedido.updateState(EstadoPedido.RECHAZADO,pedido.getUsuario(),null);

            return pedidoRepository.save(pedido);
        } else {
            pedido.updateState(EstadoPedido.ACEPTADO,pedido.getUsuario(),null);
        }

        // verificar stock

        Boolean flagStockDisponible = true;
        for (DetallePedido dp : pedido.getDetalle()) {

            Producto producto = restTemplate
                    .getForObject(gatewayURL + "/productos/api/productos/" + dp.getProducto().getId(), Producto.class);

            if (producto.getStockActual() < dp.getCantidad()) {
                log.info(dp.getProducto().getNombre() + " stock insuficiente");
                flagStockDisponible = false;
                break;
            } else {
                log.info(dp.getProducto().getNombre() + " hay stock ");
            }

        }

        // actualizacion de stock
        if (flagStockDisponible) {
            pedido.updateState(EstadoPedido.EN_PREPARACION,pedido.getUsuario(),null);
            log.info("enviando update de stock de productos");
            for (DetallePedido dp : pedido.getDetalle()) {
                log.info("Enviando {}", dp.getProducto().getId() + ";" + dp.getCantidad());
                rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_UPDATE_QUEUE,
                        dp.getProducto().getId() + ";" + dp.getCantidad());
            }
            
        }
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(String id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void deletePedido(String id) {
        pedidoRepository.deleteById(id);
    }


    public Pedido update(Pedido pedidoUpdateable, String id) {
        pedidoUpdateable.setId(id);
        return pedidoRepository.save(pedidoUpdateable);
    }
    public void restockProducts(Pedido pedido){
        log.info("enviando reestock de productos por pedido cancelado");
        for (DetallePedido dp : pedido.getDetalle()) {
            log.info("Enviando {}", dp.getProducto().getId() + ";" + dp.getCantidad());
            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_UPDATE_QUEUE,
                    dp.getProducto().getId() + ";" + (-dp.getCantidad()));
        }
    }

    public Pedido getPedidoByNroPedido(String nroPedido){
        log.info("entrado a getPedidoByNro");
        Pedido pedido = new Pedido();
        for(Pedido p: this.getAllPedidos()){
            if(p.getNumeroPedido()==Integer.parseInt(nroPedido)){
                pedido= p;
                break;
            }
        }
        log.info("saliendo de getPedidoByNro");
        if(pedido.getId()==null) return null;
        return pedido;
    }

    public Integer getMaxNumeroPedido() {
        List<Pedido> pedidos = this.getAllPedidos();
        return pedidos.stream()
                .map(Pedido::getNumeroPedido) 
                .max(Integer::compareTo)       // Encontrar el valor máximo
                .orElse(0);                    // Devolver 0 si la lista está vacía
    }

}
