package service;

import exception.*;
import model.Producto;
import java.util.List;

/** Contrato de la capa de negocio para Producto. */
public interface IProductoService {
    void    registrarProducto(Producto p)   throws TiendaException;
    void    modificarProducto(Producto p)   throws TiendaException;
    void    inactivarProducto(String codigo) throws TiendaException;
    Producto buscarProducto(String codigo)  throws TiendaException;
    List<Producto> listarActivos();
    List<Producto> listarBajoStock();
}
