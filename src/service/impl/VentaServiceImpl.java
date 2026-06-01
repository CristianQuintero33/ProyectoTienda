package service.impl;

import dao.IVentaDAO;
import exception.*;
import model.Producto;
import model.Venta;
import model.Venta.FormaPago;
import service.IClienteService;
import service.IProductoService;
import service.IVentaService;
import util.LoggerTienda;

import java.util.List;
import java.util.Map;

public class VentaServiceImpl implements IVentaService {

    private final IVentaDAO        ventaDAO;
    private final IProductoService productoService;
    private final IClienteService  clienteService;

    public VentaServiceImpl(IVentaDAO ventaDAO,
                            IProductoService productoService,
                            IClienteService  clienteService) {
        this.ventaDAO        = ventaDAO;
        this.productoService = productoService;
        this.clienteService  = clienteService;
    }

    @Override
    public Venta registrarVenta(String codigoCliente,
                                Map<String, Integer> productosYCantidades,
                                FormaPago formaPago,
                                boolean aplicaIva) throws TiendaException {
        if (productosYCantidades == null || productosYCantidades.isEmpty())
            throw new CampoObligatorioException("Productos de la venta");

        var cliente = clienteService.buscarCliente(codigoCliente);
        var venta   = new Venta(cliente, formaPago, aplicaIva);

        for (Map.Entry<String, Integer> entry : productosYCantidades.entrySet()) {
            Producto p   = productoService.buscarProducto(entry.getKey());
            int cantidad = entry.getValue();
            if (cantidad > p.getStockActual())
                throw new StockInsuficienteException(p.getNombre(), p.getStockActual(), cantidad);
            venta.agregarDetalle(p, cantidad);
        }

        ventaDAO.guardar(venta);
        LoggerTienda.info("Venta registrada: " + venta.getNumeroFactura() +
                          " | Total: $" + String.format("%.0f", venta.getTotal()));
        return venta;
    }

    @Override
    public void anularVenta(String numeroFactura) throws TiendaException {
        Venta v = ventaDAO.buscarPorFactura(numeroFactura);
        if (v == null) throw new TiendaException("Factura no encontrada: " + numeroFactura);
        v.anular();
        LoggerTienda.info("Venta anulada: " + numeroFactura);
    }

    @Override
    public List<Venta> listarVentas() { return ventaDAO.listarTodas(); }

    @Override
    public List<Venta> listarVentasPorCliente(String codigoCliente) throws TiendaException {
        clienteService.buscarCliente(codigoCliente); // valida que exista
        return ventaDAO.listarPorCliente(codigoCliente);
    }

    @Override
    public double totalVentas() {
        return ventaDAO.listarTodas().stream()
                .filter(v -> v.getEstado() == Venta.Estado.ACTIVA)
                .mapToDouble(Venta::getTotal).sum();
    }
}
