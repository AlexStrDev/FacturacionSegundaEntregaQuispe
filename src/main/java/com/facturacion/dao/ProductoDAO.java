package com.facturacion.dao;

import com.facturacion.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ProductoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Producto guardar(Producto producto) {
        if (producto.getId() == null) {
            entityManager.persist(producto);
            return producto;
        } else {
            return entityManager.merge(producto);
        }
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return entityManager.find(Producto.class, id);
    }

    @Transactional(readOnly = true)
    public Producto buscarPorCodigo(String codigo) {
        TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class);
        query.setParameter("codigo", codigo);
        List<Producto> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p ORDER BY p.nombre", Producto.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.nombre", Producto.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarConStock() {
        TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.activo = true AND p.stock > 0 ORDER BY p.nombre", Producto.class);
        return query.getResultList();
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        if (producto != null) {
            entityManager.remove(producto);
        }
    }

    @Transactional
    public Producto actualizar(Producto producto) {
        return entityManager.merge(producto);
    }
}