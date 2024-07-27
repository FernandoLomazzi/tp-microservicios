package isi.dan.ms.pedidos.Exceptions;


@SuppressWarnings("serial")
public class IlegalStateException extends Exception{
    public IlegalStateException(String msg){
        super(msg);
    }
}