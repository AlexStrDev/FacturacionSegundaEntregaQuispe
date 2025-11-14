package com.facturacion.repository;

import com.facturacion.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByRuc(String ruc);

    List<Cliente> findByActivoTrue();

    List<Cliente> findAllByOrderByIdDesc();

    List<Cliente> findByActivoTrueOrderByRazonSocial();
}