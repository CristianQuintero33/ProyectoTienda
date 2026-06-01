package exception;

public class CodigoDuplicadoException extends TiendaException {
    public CodigoDuplicadoException(String entidad, String codigo) {
        super("Ya existe un(a) " + entidad + " con el código '" + codigo + "'. Use un código diferente.");
    }
}
