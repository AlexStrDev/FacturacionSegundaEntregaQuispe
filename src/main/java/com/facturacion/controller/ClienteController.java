package com.facturacion.controller;

import com.facturacion.model.Cliente;
import com.facturacion.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Listar todos los clientes", description = "Obtiene una lista de todos los clientes registrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @Operation(summary = "Listar clientes activos", description = "Obtiene solo los clientes con estado activo")
    @ApiResponse(responseCode = "200", description = "Lista de clientes activos")
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        return ResponseEntity.ok(clienteService.obtenerActivos());
    }

    @Operation(summary = "Obtener cliente por ID", description = "Obtiene los detalles de un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener cliente por RUC", description = "Busca un cliente por su número de RUC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Cliente> obtenerPorRuc(
            @Parameter(description = "RUC del cliente", example = "20123456789") @PathVariable String ruc) {
        Cliente cliente = clienteService.obtenerPorRuc(ruc);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente")
    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteService.obtenerPorId(id);
        if (clienteExistente != null) {
            cliente.setId(id);
            return ResponseEntity.ok(clienteService.guardar(cliente));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema")
    @ApiResponse(responseCode = "204", description = "Cliente eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del cliente") @PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desactivar cliente", description = "Marca un cliente como inactivo sin eliminarlo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente desactivado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Cliente> desactivar(@Parameter(description = "ID del cliente") @PathVariable Long id) {
        Cliente cliente = clienteService.desactivar(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }
}