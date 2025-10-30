package com.facturacion.service;

import com.facturacion.dao.ClienteDAO;
import com.facturacion.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteDAO clienteDAO;

    @Autowired
    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        return clienteDAO.listarTodos();
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivos() {
        return clienteDAO.listarActivos();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteDAO.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorRuc(String ruc) {
        return clienteDAO.buscarPorRuc(ruc);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        return clienteDAO.guardar(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        clienteDAO.eliminar(id);
    }

    @Transactional
    public Cliente desactivar(Long id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente != null) {
            cliente.setActivo(false);
            return clienteDAO.actualizar(cliente);
        }
        return null;
    }
}