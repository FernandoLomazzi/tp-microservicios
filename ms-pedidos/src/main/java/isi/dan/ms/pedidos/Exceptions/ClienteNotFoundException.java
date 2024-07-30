package isi.dan.ms.pedidos.Exceptions;

@SuppressWarnings("serial")
public class ClienteNotFoundException extends Exception{
    public ClienteNotFoundException(String msg){
        super(msg);
    }
}
