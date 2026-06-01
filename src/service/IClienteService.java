package service;

import exception.*;
import model.Cliente;
import java.util.List;

public interface IClienteService {
    void    registrarCliente(Cliente c)   throws TiendaException;
    void    modificarCliente(Cliente c)   throws TiendaException;
    void    inactivarCliente(String codigo) throws TiendaException;
    Cliente buscarCliente(String codigo)  throws TiendaException;
    List<Cliente> listarActivos();
}
