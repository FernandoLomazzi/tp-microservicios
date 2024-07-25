package isi.dan.ms.pedidos.modelo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "pedidos")
@Data
public class Pedido {
    @Id
    String id;
    Instant fecha;
    Integer numeroPedido;
    String usuario;
    String observaciones;
    
    EstadoPedido estado;
    List<HistorialEstado> historialEstado;
    Obra obra;
    Cliente cliente;
    BigDecimal total;

    @Field("detalle")
    private List<DetallePedido> detalle;

}

