package exception;

public class StockInsuficienteException extends TiendaException {
    private final String nombreProducto;
    private final int    disponible;
    private final int    solicitado;

    public StockInsuficienteException(String nombreProducto, int disponible, int solicitado) {
        super("Stock insuficiente para \"" + nombreProducto +
              "\". Disponible: " + disponible + " — Solicitado: " + solicitado + ".");
        this.nombreProducto = nombreProducto;
        this.disponible     = disponible;
        this.solicitado     = solicitado;
    }

    public String getSugerencia() {
        return "Por favor reduzca la cantidad o espere reabastecimiento de \"" + nombreProducto + "\".";
    }

    public String getNombreProducto() { return nombreProducto; }
    public int    getDisponible()     { return disponible; }
    public int    getSolicitado()     { return solicitado; }
}
