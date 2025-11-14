package com.facturacion.repository;

import com.facturacion.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findAllByOrderByNombre();

    List<Producto> findByActivoTrueOrderByNombre();

    List<Producto> findByActivoTrueAndStockGreaterThanOrderByNombre(Integer stock);
}