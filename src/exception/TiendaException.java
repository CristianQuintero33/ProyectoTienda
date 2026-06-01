package exception;

// ── Excepción base de la aplicación ───────────────────────────────────────────
public class TiendaException extends Exception {
    public TiendaException(String mensaje)                    { super(mensaje); }
    public TiendaException(String mensaje, Throwable causa)   { super(mensaje, causa); }
}
