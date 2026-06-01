package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venta {

    public enum FormaPago  { EFECTIVO, TRANSFERENCIA, TARJETA, CREDITO }
    public enum Estado     { ACTIVA, ANULADA }

    // ── Detalle (línea de producto) ────────────────────────────
    public static class Detalle {
        private Producto producto;
        private int      cantidad;
        private double   precioUnitario;

        public Detalle(Producto producto, int cantidad, double precioUnitario) {
            this.producto       = producto;
            this.cantidad       = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public double getSubtotal()      { return cantidad * precioUnitario; }
        public double getSubtotalIVA()   {
            // IVA 19% solo a productos distintos de la canasta básica (simplificado)
            return getSubtotal() * 0.19;
        }

        public Producto getProducto()        { return producto; }
        public int      getCantidad()        { return cantidad; }
        public double   getPrecioUnitario()  { return precioUnitario; }
    }

    // ── Cabecera de la venta ───────────────────────────────────
    private static int consecutivo = 1;

    private String          numeroFactura;
    private LocalDateTime   fechaHora;
    private Cliente         cliente;
    private List<Detalle>   detalles;
    private FormaPago       formaPago;
    private Estado          estado;
    private boolean         aplicaIva;

    public Venta(Cliente cliente, FormaPago formaPago, boolean aplicaIva) {
        this.numeroFactura = String.format("FAC-%06d", consecutivo++);
        this.fechaHora     = LocalDateTime.now();
        this.cliente       = cliente;
        this.formaPago     = formaPago;
        this.aplicaIva     = aplicaIva;
        this.detalles      = new ArrayList<>();
        this.estado        = Estado.ACTIVA;
    }

    public void agregarDetalle(Producto p, int cantidad) {
        if (estado == Estado.ANULADA)
            throw new IllegalStateException("No se puede modificar una venta anulada.");
        detalles.add(new Detalle(p, cantidad, p.getPrecioVenta()));
        p.reducirStock(cantidad);
    }

    public double getSubtotal() {
        return detalles.stream().mapToDouble(Detalle::getSubtotal).sum();
    }

    public double getIVA() {
        return aplicaIva ? detalles.stream().mapToDouble(Detalle::getSubtotalIVA).sum() : 0;
    }

    public double getTotal() { return getSubtotal() + getIVA(); }

    public void anular() {
        if (estado == Estado.ANULADA) return;
        // Devolver stock
        for (Detalle d : detalles)
            d.getProducto().aumentarStock(d.getCantidad());
        estado = Estado.ANULADA;
    }

    public String         getNumeroFactura() { return numeroFactura; }
    public LocalDateTime  getFechaHora()     { return fechaHora; }
    public Cliente        getCliente()       { return cliente; }
    public FormaPago      getFormaPago()     { return formaPago; }
    public Estado         getEstado()        { return estado; }
    public boolean        isAplicaIva()      { return aplicaIva; }
    public List<Detalle>  getDetalles()      { return Collections.unmodifiableList(detalles); }
}
