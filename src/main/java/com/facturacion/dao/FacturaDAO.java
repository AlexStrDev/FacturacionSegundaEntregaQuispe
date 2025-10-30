package com.facturacion.dao;

import com.facturacion.model.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class FacturaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Factura guardar(Factura factura) {
        if (factura.getId() == null) {
            entityManager.persist(factura);
            return factura;
        } else {
            return entityManager.merge(factura);
        }
    }

    @Transactional(readOnly = true)
    public Factura buscarPorId(Long id) {
        return entityManager.find(Factura.class, id);
    }

    @Transactional(readOnly = true)
    public Factura buscarPorNumero(String numeroFactura) {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f WHERE f.numeroFactura = :numero", Factura.class);
        query.setParameter("numero", numeroFactura);
        List<Factura> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Transactional(readOnly = true)
    public List<Factura> listarTodas() {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f ORDER BY f.fechaEmision DESC, f.id DESC", Factura.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Factura> listarPorCliente(Long clienteId) {
        TypedQuery<Factura> query = entityManager.createQuery(
                "SELECT f FROM Factura f WHERE f.cliente.id = :clienteId ORDER BY f.fechaEmision DESC", Factura.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public Long contarFacturas() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(f) FROM Factura f", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public void eliminar(Long id) {
        Factura factura = buscarPorId(id);
        if (factura != null) {
            entityManager.remove(factura);
        }
    }
}