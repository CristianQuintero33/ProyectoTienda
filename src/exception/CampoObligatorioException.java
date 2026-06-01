package exception;

public class CampoObligatorioException extends TiendaException {
    private final String campo;
    public CampoObligatorioException(String campo) {
        super("El campo '" + campo + "' es obligatorio.");
        this.campo = campo;
    }
    public String getSugerencia() {
        return "Por favor complete el campo '" + campo + "' antes de continuar.";
    }
    public String getCampo() { return campo; }
}
