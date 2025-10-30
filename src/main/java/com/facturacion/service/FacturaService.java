package com.facturacion.service;

import com.facturacion.dao.FacturaDAO;
import com.facturacion.dao.DetalleFacturaDAO;
import com.facturacion.model.Cliente;
import com.facturacion.model.DetalleFactura;
import com.facturacion.model.Factura;
import com.facturacion.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService {

    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");

    private final FacturaDAO facturaDAO;
    private final DetalleFacturaDAO detalleFacturaDAO;
    private final ClienteService clienteService;
    private final ProductoService productoService;

    @Autowired
    public FacturaService(FacturaDAO facturaDAO, DetalleFacturaDAO detalleFacturaDAO,
                          ClienteService clienteService, ProductoService productoService) {
        this.facturaDAO = facturaDAO;
        this.detalleFacturaDAO = detalleFacturaDAO;
        this.clienteService = clienteService;
        this.productoService = productoService;
    }

    @Transactional(readOnly = true)
    public List<Factura> obtenerTodas() {
        return facturaDAO.listarTodas();
    }

    @Transactional(readOnly = true)
    public Factura obtenerPorId(Long id) {
        return facturaDAO.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public List<Factura> obtenerPorCliente(Long clienteId) {
        return facturaDAO.listarPorCliente(clienteId);
    }

    @Transactional
    public Factura crearFactura(Long clienteId, List<DetalleFacturaRequest> detalles) {
        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }

        String numeroFactura = generarNumeroFactura();
        Factura factura = new Factura(numeroFactura, cliente, LocalDate.now());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleFacturaRequest detalle : detalles) {
            Producto producto = productoService.obtenerPorId(detalle.getProductoId());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + detalle.getProductoId());
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            DetalleFactura detalleFactura = new DetalleFactura(
                    factura,
                    producto,
                    detalle.getCantidad(),
                    producto.getPrecioUnitario()
            );

            subtotal = subtotal.add(detalleFactura.getSubtotal());
            factura.getDetalles().add(detalleFactura);

            productoService.actualizarStock(producto.getId(), -detalle.getCantidad());
        }

        BigDecimal igv = subtotal.multiply(IGV_PORCENTAJE);
        BigDecimal total = subtotal.add(igv);

        factura.setSubtotal(subtotal);
        factura.setIgv(igv);
        factura.setTotal(total);

        return facturaDAO.guardar(factura);
    }

    @Transactional(readOnly = true)
    public String generarNumeroFactura() {
        Long count = facturaDAO.contarFacturas() + 1;
        return String.format("F001-%08d", count);
    }

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