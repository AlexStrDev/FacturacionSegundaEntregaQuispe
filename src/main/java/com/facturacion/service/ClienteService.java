package com.facturacion.service;

import com.facturacion.model.Cliente;
import com.facturacion.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivos() {
        return clienteRepository.findByActivoTrueOrderByRazonSocial();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorRuc(String ruc) {
        return clienteRepository.findByRuc(ruc).orElse(null);
    }

    @Transactional
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional
    public Cliente desactivar(Long id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente != null) {
            cliente.setActivo(false);
            return clienteRepository.save(cliente);
        }
        return null;
    }
}