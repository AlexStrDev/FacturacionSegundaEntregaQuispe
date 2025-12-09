package com.facturacion.service;

import com.facturacion.client.WorldClockClient;
import com.facturacion.dto.ComprobanteRequest;
import com.facturacion.dto.ComprobanteResponse;
import com.facturacion.model.Cliente;
import com.facturacion.model.DetalleFactura;
import com.facturacion.model.Factura;
import com.facturacion.model.Producto;
import com.facturacion.repository.FacturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {

    private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");

    private final FacturaRepository facturaRepository;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final WorldClockClient worldClockClient;

    @Autowired
    public FacturaService(FacturaRepository facturaRepository,
                          ClienteService clienteService,
                          ProductoService productoService,
                          WorldClockClient worldClockClient) {
        this.facturaRepository = facturaRepository;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.worldClockClient = worldClockClient;
    }

    @Transactional(readOnly = true)
    public List<Factura> obtenerTodas() {
        return facturaRepository.findAllByOrderByFechaEmisionDescIdDesc();
    }

    @Transactional(readOnly = true)
    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Factura> obtenerPorCliente(Long clienteId) {
        return facturaRepository.findByClienteIdOrderByFechaEmisionDesc(clienteId);
    }

    @Transactional
    public ComprobanteResponse crearComprobante(ComprobanteRequest request) {
        ComprobanteResponse response = new ComprobanteResponse();
        List<String> errores = new ArrayList<>();

        // Obtener fecha desde el servicio externo
        LocalDateTime fechaComprobante = worldClockClient.obtenerFechaActual();
        response.setFechaComprobante(fechaComprobante);

        // Validación 1: Cliente existente
        Long clienteId = request.getCliente().getClienteid();
        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            errores.add("Cliente con ID " + clienteId + " no encontrado");
            response.setExitoso(false);
            response.setErroresValidacion(errores);
            return response;
        }

        if (!cliente.getActivo()) {
            errores.add("Cliente con ID " + clienteId + " está inactivo");
        }

        // Validación 2 y 3: Productos existentes y stock suficiente
        List<DetalleValidado> detallesValidados = new ArrayList<>();
        int cantidadTotalProductos = 0;

        for (ComprobanteRequest.LineaProducto linea : request.getLineas()) {
            Long productoId = linea.getProducto().getProductoid();
            Integer cantidad = linea.getCantidad();

            if (cantidad == null || cantidad <= 0) {
                errores.add("Cantidad inválida para producto ID " + productoId);
                continue;
            }

            Producto producto = productoService.obtenerPorId(productoId);
            if (producto == null) {
                errores.add("Producto con ID " + productoId + " no encontrado");
                continue;
            }

            if (!producto.getActivo()) {
                errores.add("Producto '" + producto.getNombre() + "' está inactivo");
                continue;
            }

            if (producto.getStock() < cantidad) {
                errores.add("Stock insuficiente para producto '" + producto.getNombre() +
                        "'. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
                continue;
            }

            detallesValidados.add(new DetalleValidado(producto, cantidad));
            cantidadTotalProductos += cantidad;
        }

        // Si hay errores de validación, retornar sin crear factura
        if (!errores.isEmpty()) {
            response.setExitoso(false);
            response.setErroresValidacion(errores);
            return response;
        }

        try {
            // Crear factura
            String numeroFactura = generarNumeroFactura();
            Factura factura = new Factura(numeroFactura, cliente, fechaComprobante.toLocalDate());
            factura.setFechaHoraEmision(fechaComprobante);

            BigDecimal subtotal = BigDecimal.ZERO;

            // Crear detalles y actualizar stock
            for (DetalleValidado detalleValidado : detallesValidados) {
                Producto producto = detalleValidado.getProducto();
                Integer cantidad = detalleValidado.getCantidad();

                // Crear detalle con el precio actual del producto
                // Este precio queda congelado en el detalle, cambios futuros no lo afectan
                DetalleFactura detalleFactura = new DetalleFactura(
                        factura,
                        producto,
                        cantidad,
                        producto.getPrecioUnitario()
                );
                detalleFactura.setFechaCreacion(fechaComprobante);

                subtotal = subtotal.add(detalleFactura.getSubtotal());
                factura.getDetalles().add(detalleFactura);

                // Validación 4: Reducir stock
                productoService.actualizarStock(producto.getId(), -cantidad);
                logger.info("Stock reducido para producto '{}': {} unidades",
                        producto.getNombre(), cantidad);
            }

            // Calcular totales
            BigDecimal igv = subtotal.multiply(IGV_PORCENTAJE);
            BigDecimal total = subtotal.add(igv);

            factura.setSubtotal(subtotal);
            factura.setIgv(igv);
            factura.setTotal(total);
            factura.setCantidadTotalProductos(cantidadTotalProductos);

            // Guardar factura
            Factura facturaGuardada = facturaRepository.save(factura);

            // Construir respuesta exitosa
            response.setExitoso(true);
            response.setPrecioTotal(total);
            response.setCantidadProductos(cantidadTotalProductos);
            response.setComprobante(ComprobanteResponse.ComprobanteData.fromFactura(facturaGuardada));

            logger.info("Comprobante creado exitosamente: {}", numeroFactura);

        } catch (Exception e) {
            logger.error("Error al crear comprobante", e);
            errores.add("Error al procesar el comprobante: " + e.getMessage());
            response.setExitoso(false);
            response.setErroresValidacion(errores);
        }

        return response;
    }

    @Transactional(readOnly = true)
    public String generarNumeroFactura() {
        Long count = facturaRepository.count() + 1;
        return String.format("F001-%08d", count);
    }

    private static class DetalleValidado {
        private final Producto producto;
        private final Integer cantidad;

        public DetalleValidado(Producto producto, Integer cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }
    }

    @Deprecated
    public static class DetalleFacturaRequest {
        private Long productoId;
        private Integer cantidad;

        public DetalleFacturaRequest() {
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}