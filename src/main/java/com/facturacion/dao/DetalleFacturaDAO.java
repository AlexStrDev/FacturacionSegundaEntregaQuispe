package com.facturacion.dao;

import com.facturacion.model.DetalleFactura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DetalleFacturaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public DetalleFactura guardar(DetalleFactura detalle) {
        if (detalle.getId() == null) {
            entityManager.persist(detalle);
            return detalle;
        } else {
            return entityManager.merge(detalle);
        }
    }

    @Transactional(readOnly = true)
    public DetalleFactura buscarPorId(Long id) {
        return entityManager.find(DetalleFactura.class, id);
    }

    @Transactional(readOnly = true)
    public List<DetalleFactura> listarPorFactura(Long facturaId) {
        TypedQuery<DetalleFactura> query = entityManager.createQuery(
                "SELECT d FROM DetalleFactura d WHERE d.factura.id = :facturaId", DetalleFactura.class);
        query.setParameter("facturaId", facturaId);
        return query.getResultList();
    }

    @Transactional
    public void eliminar(Long id) {
        DetalleFactura detalle = buscarPorId(id);
        if (detalle != null) {
            entityManager.remove(detalle);
        }
    }
}