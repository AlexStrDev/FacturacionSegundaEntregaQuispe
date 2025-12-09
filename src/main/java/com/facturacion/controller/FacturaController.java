package com.facturacion.controller;

import com.facturacion.dto.ComprobanteRequest;
import com.facturacion.dto.ComprobanteResponse;
import com.facturacion.model.Factura;
import com.facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturas/Comprobantes", description = "API para gestión de facturas y comprobantes")
public class FacturaController {

    private final FacturaService facturaService;

    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @Operation(
            summary = "Listar todas las facturas",
            description = "Obtiene una lista de todas las facturas registradas ordenadas por fecha"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de facturas obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<List<Factura>> listarTodas() {
        return ResponseEntity.ok(facturaService.obtenerTodas());
    }

    @Operation(
            summary = "Obtener factura por ID",
            description = "Obtiene los detalles de una factura específica por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Factura encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Factura no encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(
            @Parameter(description = "ID de la factura", required = true, example = "1")
            @PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura != null) {
            return ResponseEntity.ok(factura);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Obtener facturas por cliente",
            description = "Obtiene todas las facturas de un cliente específico"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de facturas del cliente"
    )
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Factura>> obtenerPorCliente(
            @Parameter(description = "ID del cliente", required = true, example = "1")
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.obtenerPorCliente(clienteId));
    }

    @Operation(
            summary = "Crear nuevo comprobante",
            description = "Crea un nuevo comprobante de venta con validaciones completas:\n" +
                    "- Valida existencia del cliente\n" +
                    "- Valida existencia de productos\n" +
                    "- Valida stock disponible\n" +
                    "- Reduce stock automáticamente\n" +
                    "- Obtiene fecha desde servicio externo\n" +
                    "- Calcula totales e IGV"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comprobante creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación",
                    content = @Content(schema = @Schema(implementation = ComprobanteResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<ComprobanteResponse> crearComprobante(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del comprobante a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ComprobanteRequest.class),
                            mediaType = "application/json"
                    )
            )
            @Valid @RequestBody ComprobanteRequest request) {

        ComprobanteResponse response = facturaService.crearComprobante(request);

        if (response.isExitoso()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}