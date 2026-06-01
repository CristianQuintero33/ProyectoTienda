package exception;

/** Producto no encontrado por código. */
public class ProductoNoEncontradoException extends TiendaException {
    public ProductoNoEncontradoException(String codigo) {
        super("No se encontró el producto con código: '" + codigo + "'.");
    }
}
