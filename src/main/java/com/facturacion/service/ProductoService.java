package com.facturacion.service;

import com.facturacion.dao.ProductoDAO;
import com.facturacion.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoDAO productoDAO;

    @Autowired
    public ProductoService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoDAO.listarTodos();
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoDAO.listarActivos();
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerConStock() {
        return productoDAO.listarConStock();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoDAO.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorCodigo(String codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoDAO.guardar(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        productoDAO.eliminar(id);
    }

    @Transactional
    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = obtenerPorId(id);
        if (producto != null) {
            producto.setStock(producto.getStock() + cantidad);
            return productoDAO.actualizar(producto);
        }
        return null;
    }
}