package isi.dan.ms.pedidos.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import isi.dan.ms.pedidos.controller.PedidoController;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoObra;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;
import isi.dan.ms.pedidos.servicio.PedidoService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
public class PedidosControllerTests {

    @Autowired
	private MockMvc mockMvc;

	@MockBean
	private PedidoService PedidoService;

	private Cliente cliente;
    private Pedido pedido;
    private Obra obra;
    private Producto producto;
    @BeforeEach
	void setUp() {
        //cliente
        cliente = new Cliente();
        cliente.setId(101);
        cliente.setNombre("Juan Pérez");
        cliente.setCorreoElectronico("juan.perez@example.com");
        cliente.setCuit("20-12345678-9");
        cliente.setMaximoDescubierto(BigDecimal.valueOf(150000));
        //obra
        obra = new Obra();
        obra.setId(1);
        obra.setDireccion("Calle Falsa 123");
        obra.setEsRemodelacion(false);
        obra.setLat((float) -34.6037);
        obra.setLng((float) -58.3816);
        obra.setCliente(cliente);
        obra.setPresupuesto(new BigDecimal("500000.00"));
        obra.setEstado(EstadoObra.PENDIENTE);
        //producto
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descripción del producto de prueba");
        producto.setPrecio(new BigDecimal("200.00"));
        producto.setStockActual(50);
        //detalle
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setProducto(producto);
        detallePedido.setCantidad(5);
        detallePedido.setPrecioUnitario(new BigDecimal("200.00"));
        detallePedido.setDescuento(new BigDecimal("10.00"));
        detallePedido.setPrecioFinal(new BigDecimal("990.00"));

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detallePedido);
		//pedido
        pedido = new Pedido();
        pedido.setId("pedido123");
        pedido.setFecha(Instant.now());
        pedido.setNumeroPedido(12345);
        pedido.setUsuario("usuario1");
        pedido.setObservaciones("Observaciones del pedido");
        pedido.setEstado(EstadoPedido.ACEPTADO);
       
        pedido.setObra(obra);
        pedido.setCliente(cliente);
        pedido.setTotal(new BigDecimal("1000.00"));
        pedido.setDetalle(detalles);
	}

    @Test
	void testGetAll() throws Exception {
		Mockito.when(PedidoService.getAllPedidos()).thenReturn(Collections.singletonList(pedido));

		mockMvc.perform(get("/api/pedidos")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].usuario").value("usuario1"))
				.andExpect(jsonPath("$[0].observaciones").value("Observaciones del pedido"))
				.andExpect(jsonPath("$[0].estado").value("ACEPTADO"));
	}



}
