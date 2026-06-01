package service;

import exception.*;
import model.Venta;
import model.Venta.FormaPago;
import java.util.List;
import java.util.Map;

public interface IVentaService {
    Venta registrarVenta(String codigoCliente,
                         Map<String, Integer> productosYCantidades,
                         FormaPago formaPago,
                         boolean aplicaIva) throws TiendaException;
    void  anularVenta(String numeroFactura) throws TiendaException;
    List<Venta> listarVentas();
    List<Venta> listarVentasPorCliente(String codigoCliente) throws TiendaException;
    double totalVentas();
}
