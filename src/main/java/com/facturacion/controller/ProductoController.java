package com.facturacion.controller;

import com.facturacion.model.Producto;
import com.facturacion.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.obtenerActivos());
    }

    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> listarConStock() {
        return ResponseEntity.ok(productoService.obtenerConStock());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Producto> obtenerPorCodigo(@PathVariable String codigo) {
        Producto producto = productoService.obtenerPorCodigo(codigo);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.guardar(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        Producto producto = productoService.actualizarStock(id, cantidad);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }
}