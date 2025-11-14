package com.facturacion.service;

import com.facturacion.model.Producto;
import com.facturacion.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAllByOrderByNombre();
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrueOrderByNombre();
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerConStock() {
        return productoRepository.findByActivoTrueAndStockGreaterThanOrderByNombre(0);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo).orElse(null);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional
    public Producto actualizarStock(Long id, Integer cantidad) {
        Producto producto = obtenerPorId(id);
        if (producto != null) {
            producto.setStock(producto.getStock() + cantidad);
            return productoRepository.save(producto);
        }
        return null;
    }
}