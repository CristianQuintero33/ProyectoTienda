package dao.impl;

import dao.IProductoDAO;
import exception.CodigoDuplicadoException;
import exception.ProductoNoEncontradoException;
import model.Producto;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del IProductoDAO.
 * Patrón: capa de datos desacoplada de la capa de negocio mediante interfaz.
 */
public class ProductoDAOMemoria implements IProductoDAO {

    private final Map<String, Producto> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Producto p) throws CodigoDuplicadoException {
        if (almacen.containsKey(p.getCodigo()))
            throw new CodigoDuplicadoException("producto", p.getCodigo());
        almacen.put(p.getCodigo(), p);
    }

    @Override
    public void actualizar(Producto p) throws ProductoNoEncontradoException {
        if (!almacen.containsKey(p.getCodigo()))
            throw new ProductoNoEncontradoException(p.getCodigo());
        almacen.put(p.getCodigo(), p);
    }

    @Override
    public void inactivar(String codigo) throws ProductoNoEncontradoException {
        Producto p = buscarPorCodigo(codigo);
        p.setActivo(false);
    }

    @Override
    public Producto buscarPorCodigo(String codigo) throws ProductoNoEncontradoException {
        Producto p = almacen.get(codigo);
        if (p == null) throw new ProductoNoEncontradoException(codigo);
        return p;
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public List<Producto> listarActivos() {
        return almacen.values().stream()
                .filter(Producto::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarBajoStock() {
        return almacen.values().stream()
                .filter(p -> p.isActivo() && p.isBajoStock())
                .collect(Collectors.toList());
    }
}
