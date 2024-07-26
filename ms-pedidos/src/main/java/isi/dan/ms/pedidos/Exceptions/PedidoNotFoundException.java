package isi.dan.ms.pedidos.Exceptions;

@SuppressWarnings("serial")
public class PedidoNotFoundException extends Exception{
    public PedidoNotFoundException(String msg){
        super(msg);
    }
}
