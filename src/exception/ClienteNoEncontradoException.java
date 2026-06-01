package exception;

public class ClienteNoEncontradoException extends TiendaException {
    public ClienteNoEncontradoException(String codigo) {
        super("No se encontró el cliente con código: '" + codigo + "'.");
    }
}
