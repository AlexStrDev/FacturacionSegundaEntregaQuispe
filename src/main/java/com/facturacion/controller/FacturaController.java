package com.facturacion.controller;

import com.facturacion.model.Factura;
import com.facturacion.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listarTodas() {
        return ResponseEntity.ok(facturaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura != null) {
            return ResponseEntity.ok(factura);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Factura>> obtenerPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.obtenerPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody FacturaRequest request) {
        try {
            Factura factura = facturaService.crearFactura(request.getClienteId(), request.getDetalles());
            return ResponseEntity.status(HttpStatus.CREATED).body(factura);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    public static class FacturaRequest {
        private Long clienteId;
        private List<FacturaService.DetalleFacturaRequest> detalles;

        public FacturaRequest() {
        }

        public Long getClienteId() {
            return clienteId;
        }

        public void setClienteId(Long clienteId) {
            this.clienteId = clienteId;
        }

        public List<FacturaService.DetalleFacturaRequest> getDetalles() {
            return detalles;
        }

        public void setDetalles(List<FacturaService.DetalleFacturaRequest> detalles) {
            this.detalles = detalles;
        }
    }

    public static class ErrorResponse {
        private String mensaje;

        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}