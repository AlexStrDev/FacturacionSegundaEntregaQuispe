package com.facturacion.controller;

import com.facturacion.model.Producto;
import com.facturacion.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista de todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Listar productos activos", description = "Obtiene solo los productos con estado activo")
    @ApiResponse(responseCode = "200", description = "Lista de productos activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        return ResponseEntity.ok(productoService.obtenerActivos());
    }

    @Operation(summary = "Listar productos con stock", description = "Obtiene productos activos que tienen stock disponible")
    @ApiResponse(responseCode = "200", description = "Lista de productos con stock")
    @GetMapping("/stock")
    public ResponseEntity<List<Producto>> listarConStock() {
        return ResponseEntity.ok(productoService.obtenerConStock());
    }

    @Operation(summary = "Obtener producto por ID", description = "Obtiene los detalles de un producto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Long id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener producto por código", description = "Busca un producto por su código único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Producto> obtenerPorCodigo(
            @Parameter(description = "Código del producto", example = "PROD001") @PathVariable String codigo) {
        Producto producto = productoService.obtenerPorCodigo(codigo);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @RequestBody Producto producto) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.guardar(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponse(responseCode = "204", description = "Producto eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del producto") @PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar stock",
            description = "Actualiza la cantidad de stock de un producto (puede ser positivo para incrementar o negativo para reducir)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad a modificar (positivo o negativo)", example = "10") @RequestParam Integer cantidad) {
        Producto producto = productoService.actualizarStock(id, cantidad);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }
}