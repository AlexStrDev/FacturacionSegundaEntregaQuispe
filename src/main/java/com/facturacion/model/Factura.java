package com.facturacion.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_factura", unique = true, nullable = false, length = 20)
    private String numeroFactura;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_hora_emision")
    private LocalDateTime fechaHoraEmision;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "cantidad_total_productos")
    private Integer cantidadTotalProductos;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() {
    }

    public Factura(String numeroFactura, Cliente cliente, LocalDate fechaEmision) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.fechaEmision = fechaEmision;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
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

    public Integer getCantidadTotalProductos() {
        return cantidadTotalProductos;
    }

    public void setCantidadTotalProductos(Integer cantidadTotalProductos) {
        this.cantidadTotalProductos = cantidadTotalProductos;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", cliente=" + (cliente != null ? cliente.getRazonSocial() : "null") +
                ", fechaEmision=" + fechaEmision +
                ", fechaHoraEmision=" + fechaHoraEmision +
                ", subtotal=" + subtotal +
                ", igv=" + igv +
                ", total=" + total +
                ", cantidadTotalProductos=" + cantidadTotalProductos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(id, factura.id) &&
                Objects.equals(numeroFactura, factura.numeroFactura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroFactura);
    }
}