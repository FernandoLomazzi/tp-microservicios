package isi.dan.msclientes.exception;

@SuppressWarnings("serial")
public class ClienteNotFoundException extends Exception{
    public ClienteNotFoundException(String msg){
        super(msg);
    }
}
