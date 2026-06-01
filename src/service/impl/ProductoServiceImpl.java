package service.impl;

import dao.IProductoDAO;
import exception.*;
import model.Producto;
import service.IProductoService;
import util.Validador;
import util.LoggerTienda;

import java.util.List;

/**
 * Implementación de la lógica de negocio para Producto.
 * Aplica validaciones (Semana 4) antes de delegar al DAO.
 */
public class ProductoServiceImpl implements IProductoService {

    private final IProductoDAO dao;

    public ProductoServiceImpl(IProductoDAO dao) { this.dao = dao; }

    @Override
    public void registrarProducto(Producto p) throws TiendaException {
        validar(p);
        try {
            dao.guardar(p);
            LoggerTienda.info("Producto registrado: " + p.getCodigo());
        } catch (CodigoDuplicadoException e) {
            LoggerTienda.advertencia("Intento de código duplicado: " + p.getCodigo());
            throw e;
        }
    }

    @Override
    public void modificarProducto(Producto p) throws TiendaException {
        validar(p);
        dao.actualizar(p);
        LoggerTienda.info("Producto actualizado: " + p.getCodigo());
    }

    @Override
    public void inactivarProducto(String codigo) throws TiendaException {
        dao.inactivar(codigo);
        LoggerTienda.info("Producto inactivado: " + codigo);
    }

    @Override
    public Producto buscarProducto(String codigo) throws TiendaException {
        return dao.buscarPorCodigo(codigo);
    }

    @Override
    public List<Producto> listarActivos()    { return dao.listarActivos(); }

    @Override
    public List<Producto> listarBajoStock()  { return dao.listarBajoStock(); }

    // ── Validaciones internas (Semana 4) ─────────────────────────
    private void validar(Producto p) throws CampoObligatorioException {
        Validador.requerirTexto(p.getCodigo(),  "Código");
        Validador.requerirTexto(p.getNombre(),  "Nombre");
        if (p.getCategoria() == null)
            throw new CampoObligatorioException("Categoría");
        if (p.getPrecioCompra() <= 0)
            throw new CampoObligatorioException("Precio de compra (debe ser > 0)");
        if (p.getPrecioVenta() <= 0)
            throw new CampoObligatorioException("Precio de venta (debe ser > 0)");
        if (p.getStockMinimo() < 0)
            throw new CampoObligatorioException("Stock mínimo (debe ser >= 0)");
    }
}
