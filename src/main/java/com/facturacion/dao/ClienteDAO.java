package com.facturacion.dao;

import com.facturacion.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ClienteDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Cliente guardar(Cliente cliente) {
        if (cliente.getId() == null) {
            entityManager.persist(cliente);
            return cliente;
        } else {
            return entityManager.merge(cliente);
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return entityManager.find(Cliente.class, id);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorRuc(String ruc) {
        TypedQuery<Cliente> query = entityManager.createQuery(
                "SELECT c FROM Cliente c WHERE c.ruc = :ruc", Cliente.class);
        query.setParameter("ruc", ruc);
        List<Cliente> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        TypedQuery<Cliente> query = entityManager.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.id DESC", Cliente.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        TypedQuery<Cliente> query = entityManager.createQuery(
                "SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.razonSocial", Cliente.class);
        return query.getResultList();
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
    }

    @Transactional
    public Cliente actualizar(Cliente cliente) {
        return entityManager.merge(cliente);
    }
}