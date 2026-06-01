package model;

/**
 * Entidad Producto — Tienda Minorista
 * Campos según documento DAD del caso de estudio.
 */
public class Producto {

    public enum Categoria { VIVERES, ASEO, PAPELERIA, OTRO }

    private String  codigo;
    private String  nombre;
    private Categoria categoria;
    private double  precioCompra;
    private double  precioVenta;
    private int     stockActual;
    private int     stockMinimo;
    private int     stockMaximo;
    private boolean activo;

    public Producto() { this.activo = true; }

    public Producto(String codigo, String nombre, Categoria categoria,
                    double precioCompra, double precioVenta,
                    int stockActual, int stockMinimo, int stockMaximo) {
        this.codigo       = codigo;
        this.nombre       = nombre;
        this.categoria    = categoria;
        this.precioCompra = precioCompra;
        this.precioVenta  = precioVenta;
        this.stockActual  = stockActual;
        this.stockMinimo  = stockMinimo;
        this.stockMaximo  = stockMaximo;
        this.activo       = true;
    }

    // ── Lógica de negocio ─────────────────────────────────────
    public boolean isBajoStock()     { return stockActual <= stockMinimo; }
    public double  getMargenUtilidad(){ return precioVenta - precioCompra; }
    public double  getValorInventario(){ return precioCompra * stockActual; }

    public void reducirStock(int cantidad) {
        if (cantidad > stockActual)
            throw new IllegalStateException("Stock insuficiente para \"" + nombre + "\"");
        stockActual -= cantidad;
    }

    public void aumentarStock(int cantidad) {
        stockActual += cantidad;
    }

    // ── Getters / Setters ──────────────────────────────────────
    public String    getCodigo()        { return codigo; }
    public String    getNombre()        { return nombre; }
    public Categoria getCategoria()     { return categoria; }
    public double    getPrecioCompra()  { return precioCompra; }
    public double    getPrecioVenta()   { return precioVenta; }
    public int       getStockActual()   { return stockActual; }
    public int       getStockMinimo()   { return stockMinimo; }
    public int       getStockMaximo()   { return stockMaximo; }
    public boolean   isActivo()         { return activo; }

    public void setCodigo(String c)          { this.codigo = c; }
    public void setNombre(String n)          { this.nombre = n; }
    public void setCategoria(Categoria c)    { this.categoria = c; }
    public void setPrecioCompra(double p)    { this.precioCompra = p; }
    public void setPrecioVenta(double p)     { this.precioVenta = p; }
    public void setStockActual(int s)        { this.stockActual = s; }
    public void setStockMinimo(int s)        { this.stockMinimo = s; }
    public void setStockMaximo(int s)        { this.stockMaximo = s; }
    public void setActivo(boolean a)         { this.activo = a; }

    @Override
    public String toString() { return "[" + codigo + "] " + nombre; }
}
