package dao.impl;

import dao.IClienteDAO;
import exception.ClienteNoEncontradoException;
import exception.CodigoDuplicadoException;
import model.Cliente;

import java.util.*;
import java.util.stream.Collectors;

public class ClienteDAOMemoria implements IClienteDAO {

    private final Map<String, Cliente> almacen = new LinkedHashMap<>();

    @Override
    public void guardar(Cliente c) throws CodigoDuplicadoException {
        if (almacen.containsKey(c.getCodigo()))
            throw new CodigoDuplicadoException("cliente", c.getCodigo());
        almacen.put(c.getCodigo(), c);
    }

    @Override
    public void actualizar(Cliente c) throws ClienteNoEncontradoException {
        if (!almacen.containsKey(c.getCodigo()))
            throw new ClienteNoEncontradoException(c.getCodigo());
        almacen.put(c.getCodigo(), c);
    }

    @Override
    public void inactivar(String codigo) throws ClienteNoEncontradoException {
        Cliente c = buscarPorCodigo(codigo);
        c.setActivo(false);
    }

    @Override
    public Cliente buscarPorCodigo(String codigo) throws ClienteNoEncontradoException {
        Cliente c = almacen.get(codigo);
        if (c == null) throw new ClienteNoEncontradoException(codigo);
        return c;
    }

    @Override
    public List<Cliente> listarTodos()   { return new ArrayList<>(almacen.values()); }

    @Override
    public List<Cliente> listarActivos() {
        return almacen.values().stream()
                .filter(Cliente::isActivo)
                .collect(Collectors.toList());
    }
}
