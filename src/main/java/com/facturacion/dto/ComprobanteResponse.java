package com.facturacion.dto;

import com.facturacion.model.Factura;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Respuesta de creación de comprobante")
public class ComprobanteResponse {

    @Schema(description = "Indica si el comprobante se creó exitosamente")
    private boolean exitoso;

    @Schema(description = "Fecha y hora del comprobante obtenida del servicio externo")
    private LocalDateTime fechaComprobante;

    @Schema(description = "Precio total de la venta")
    private BigDecimal precioTotal;

    @Schema(description = "Cantidad total de productos vendidos")
    private Integer cantidadProductos;

    @Schema(description = "Lista de errores de validación")
    private List<String> erroresValidacion;

    @Schema(description = "Datos del comprobante generado")
    private ComprobanteData comprobante;

    public ComprobanteResponse() {
        this.erroresValidacion = new ArrayList<>();
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public LocalDateTime getFechaComprobante() {
        return fechaComprobante;
    }

    public void setFechaComprobante(LocalDateTime fechaComprobante) {
        this.fechaComprobante = fechaComprobante;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public List<String> getErroresValidacion() {
        return erroresValidacion;
    }

    public void setErroresValidacion(List<String> erroresValidacion) {
        this.erroresValidacion = erroresValidacion;
    }

    public void agregarError(String error) {
        this.erroresValidacion.add(error);
    }

    public ComprobanteData getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteData comprobante) {
        this.comprobante = comprobante;
    }

    @Schema(description = "Datos del comprobante")
    public static class ComprobanteData {
        private Long id;
        private String numeroFactura;
        private String cliente;
        private BigDecimal subtotal;
        private BigDecimal igv;
        private BigDecimal total;
        private List<LineaDetalle> detalles;

        public ComprobanteData() {
        }

        public static ComprobanteData fromFactura(Factura factura) {
            ComprobanteData data = new ComprobanteData();
            data.setId(factura.getId());
            data.setNumeroFactura(factura.getNumeroFactura());
            data.setCliente(factura.getCliente().getRazonSocial());
            data.setSubtotal(factura.getSubtotal());
            data.setIgv(factura.getIgv());
            data.setTotal(factura.getTotal());

            List<LineaDetalle> detalles = new ArrayList<>();
            factura.getDetalles().forEach(detalle -> {
                LineaDetalle linea = new LineaDetalle();
                linea.setProducto(detalle.getProducto().getNombre());
                linea.setCantidad(detalle.getCantidad());
                linea.setPrecioUnitario(detalle.getPrecioUnitario());
                linea.setSubtotal(detalle.getSubtotal());
                detalles.add(linea);
            });
            data.setDetalles(detalles);

            return data;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNumeroFactura() {
            return numeroFactura;
        }

        public void setNumeroFactura(String numeroFactura) {
            this.numeroFactura = numeroFactura;
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getIgv() {
            return igv;
        }

        public void setIgv(BigDecimal igv) {
            this.igv = igv;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public List<LineaDetalle> getDetalles() {
            return detalles;
        }

        public void setDetalles(List<LineaDetalle> detalles) {
            this.detalles = detalles;
        }
    }

    @Schema(description = "Detalle de línea de producto")
    public static class LineaDetalle {
        private String producto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        public LineaDetalle() {
        }

        public String getProducto() {
            return producto;
        }

        public void setProducto(String producto) {
            this.producto = producto;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }
    }
}