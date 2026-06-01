package dao.impl;

import dao.IVentaDAO;
import model.Venta;

import java.util.*;
import java.util.stream.Collectors;

public class VentaDAOMemoria implements IVentaDAO {

    private final Map<String, Venta> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Venta v) { almacen.put(v.getNumeroFactura(), v); }

    @Override
    public Venta buscarPorFactura(String numeroFactura) {
        return almacen.get(numeroFactura);
    }

    @Override
    public List<Venta> listarTodas() { return new ArrayList<>(almacen.values()); }

    @Override
    public List<Venta> listarPorCliente(String codigoCliente) {
        return almacen.values().stream()
                .filter(v -> v.getCliente().getCodigo().equals(codigoCliente))
                .collect(Collectors.toList());
    }
}
