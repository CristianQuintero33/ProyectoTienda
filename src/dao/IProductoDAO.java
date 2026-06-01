package dao;

import exception.CodigoDuplicadoException;
import exception.ProductoNoEncontradoException;
import model.Producto;
import java.util.List;

/**
 * Contrato DAO para Producto — patrón DAO con interfaces (Semana 5).
 * Define QUÉ operaciones existen; la implementación define el CÓMO.
 */
public interface IProductoDAO {
    void    guardar(Producto p)  throws CodigoDuplicadoException;
    void    actualizar(Producto p) throws ProductoNoEncontradoException;
    void    inactivar(String codigo) throws ProductoNoEncontradoException;
    Producto buscarPorCodigo(String codigo) throws ProductoNoEncontradoException;
    List<Producto> listarTodos();
    List<Producto> listarActivos();
    List<Producto> listarBajoStock();
}
