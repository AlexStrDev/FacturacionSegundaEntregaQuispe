package com.facturacion.controller;

import com.facturacion.model.Cliente;
import com.facturacion.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        return ResponseEntity.ok(clienteService.obtenerActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Cliente> obtenerPorRuc(@PathVariable String ruc) {
        Cliente cliente = clienteService.obtenerPorRuc(ruc);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente clienteExistente = clienteService.obtenerPorId(id);
        if (clienteExistente != null) {
            cliente.setId(id);
            return ResponseEntity.ok(clienteService.guardar(cliente));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Cliente> desactivar(@PathVariable Long id) {
        Cliente cliente = clienteService.desactivar(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }
}