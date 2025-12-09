package com.facturacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request para crear un comprobante")
public class ComprobanteRequest {

    @NotNull(message = "El cliente es obligatorio")
    @Valid
    @Schema(description = "Información del cliente")
    private ClienteRef cliente;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    @Schema(description = "Lista de líneas de productos")
    private List<LineaProducto> lineas;

    public ComprobanteRequest() {
    }

    public ClienteRef getCliente() {
        return cliente;
    }

    public void setCliente(ClienteRef cliente) {
        this.cliente = cliente;
    }

    public List<LineaProducto> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaProducto> lineas) {
        this.lineas = lineas;
    }

    @Schema(description = "Referencia al cliente")
    public static class ClienteRef {
        @NotNull(message = "El ID del cliente es obligatorio")
        @Schema(description = "ID del cliente", example = "1")
        private Long clienteid;

        public ClienteRef() {
        }

        public Long getClienteid() {
            return clienteid;
        }

        public void setClienteid(Long clienteid) {
            this.clienteid = clienteid;
        }
    }

    @Schema(description = "Línea de producto en el comprobante")
    public static class LineaProducto {
        @NotNull(message = "La cantidad es obligatoria")
        @Schema(description = "Cantidad de productos", example = "5", minimum = "1")
        private Integer cantidad;

        @NotNull(message = "El producto es obligatorio")
        @Valid
        @Schema(description = "Referencia al producto")
        private ProductoRef producto;

        public LineaProducto() {
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public ProductoRef getProducto() {
            return producto;
        }

        public void setProducto(ProductoRef producto) {
            this.producto = producto;
        }
    }

    @Schema(description = "Referencia al producto")
    public static class ProductoRef {
        @NotNull(message = "El ID del producto es obligatorio")
        @Schema(description = "ID del producto", example = "1")
        private Long productoid;

        public ProductoRef() {
        }

        public Long getProductoid() {
            return productoid;
        }

        public void setProductoid(Long productoid) {
            this.productoid = productoid;
        }
    }
}