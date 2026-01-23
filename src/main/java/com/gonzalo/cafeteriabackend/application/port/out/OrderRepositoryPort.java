package com.gonzalo.cafeteriabackend.application.port.out;

import com.gonzalo.cafeteriabackend.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    List<Order> findPendingOrdersWithItems();

    List<Order> findAllActiveByTable(Long tableId);

    Optional<Order> findById(Long id);

    Order save(Order order);

    void saveAndFlush(Order order);
}
