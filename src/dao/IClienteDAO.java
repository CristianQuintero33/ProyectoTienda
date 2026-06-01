package dao;

import exception.ClienteNoEncontradoException;
import exception.CodigoDuplicadoException;
import model.Cliente;
import java.util.List;

public interface IClienteDAO {
    void    guardar(Cliente c)   throws CodigoDuplicadoException;
    void    actualizar(Cliente c) throws ClienteNoEncontradoException;
    void    inactivar(String codigo) throws ClienteNoEncontradoException;
    Cliente buscarPorCodigo(String codigo) throws ClienteNoEncontradoException;
    List<Cliente> listarTodos();
    List<Cliente> listarActivos();
}
