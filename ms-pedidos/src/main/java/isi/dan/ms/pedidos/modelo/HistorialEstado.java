package isi.dan.ms.pedidos.modelo;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "historialesEstado")
public class HistorialEstado {

    EstadoPedido estado;
    Instant fechaEstado;
    String userEstado;
    String detalle;
}
