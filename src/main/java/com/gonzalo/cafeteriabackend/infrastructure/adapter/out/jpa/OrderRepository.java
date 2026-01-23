package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // COCINERO: Busca órdenes con estado PENDIENTE.
    // DISTINCT y JOIN FETCH evitan duplicados y errores de carga de productos.
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p WHERE o.status = 'PENDIENTE'")
    List<Order> findPendingOrdersWithItems();

    // MOZO: Busca todas las órdenes que no estén cerradas para una mesa específica.
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p " +
            "WHERE o.table.id = :tableId AND o.status <> 'CLOSED' ORDER BY o.id ASC")
    List<Order> findAllActiveByTable(@Param("tableId") Long tableId);
}
