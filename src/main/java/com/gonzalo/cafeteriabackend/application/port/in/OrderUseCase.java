package com.gonzalo.cafeteriabackend.application.port.in;

import com.gonzalo.cafeteriabackend.domain.model.Order;
import java.util.List;

public interface OrderUseCase {
    Order getConsumoAcumulado(Long tableId);

    List<Order> getPendingOrders();

    Order createOrder(Order order);

    void updateStatus(Long orderId, String status);

    void closeOrder(Long tableId);
}
