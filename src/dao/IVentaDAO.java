package dao;

import model.Venta;
import java.util.List;

public interface IVentaDAO {
    void  guardar(Venta v);
    Venta buscarPorFactura(String numeroFactura);
    List<Venta> listarTodas();
    List<Venta> listarPorCliente(String codigoCliente);
}
