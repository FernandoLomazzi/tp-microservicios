package isi.dan.ms.pedidos.modelo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
    List<HistorialEstado> historialEstado = new ArrayList<HistorialEstado>();
    Obra obra;
    Cliente cliente;
    BigDecimal total;

    @Field("detalle")
    private List<DetallePedido> detalle;

    public void updateState(EstadoPedido nuevoEstado, String userEstado, String detalle){
    if(this.estado==null){
        historialEstado.add(new HistorialEstado(nuevoEstado, userEstado, detalle));
    }
    historialEstado.add(new HistorialEstado(this.estado, userEstado, detalle));
    this.estado= nuevoEstado;

    }

}

