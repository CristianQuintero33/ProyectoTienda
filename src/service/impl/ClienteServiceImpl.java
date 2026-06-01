package service.impl;

import dao.IClienteDAO;
import exception.*;
import model.Cliente;
import service.IClienteService;
import util.Validador;
import util.LoggerTienda;

import java.util.List;

public class ClienteServiceImpl implements IClienteService {

    private final IClienteDAO dao;

    public ClienteServiceImpl(IClienteDAO dao) { this.dao = dao; }

    @Override
    public void registrarCliente(Cliente c) throws TiendaException {
        validar(c);
        try {
            dao.guardar(c);
            LoggerTienda.info("Cliente registrado: " + c.getCodigo());
        } catch (CodigoDuplicadoException e) {
            LoggerTienda.advertencia("Código duplicado de cliente: " + c.getCodigo());
            throw e;
        }
    }

    @Override
    public void modificarCliente(Cliente c) throws TiendaException {
        validar(c);
        dao.actualizar(c);
        LoggerTienda.info("Cliente actualizado: " + c.getCodigo());
    }

    @Override
    public void inactivarCliente(String codigo) throws TiendaException {
        dao.inactivar(codigo);
        LoggerTienda.info("Cliente inactivado: " + codigo);
    }

    @Override
    public Cliente buscarCliente(String codigo) throws TiendaException {
        return dao.buscarPorCodigo(codigo);
    }

    @Override
    public List<Cliente> listarActivos() { return dao.listarActivos(); }

    private void validar(Cliente c) throws CampoObligatorioException {
        Validador.requerirTexto(c.getCodigo(),               "Código");
        Validador.requerirTexto(c.getNombreCompleto(),       "Nombre completo");
        Validador.requerirTexto(c.getNumeroIdentificacion(), "Número de identificación");
        Validador.requerirTexto(c.getTelefono(),             "Teléfono");
        if (c.getTipoIdentificacion() == null)
            throw new CampoObligatorioException("Tipo de identificación");
        if (c.getTipoCliente() == null)
            throw new CampoObligatorioException("Tipo de cliente");
    }
}
