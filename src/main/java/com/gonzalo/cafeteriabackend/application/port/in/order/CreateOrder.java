package com.gonzalo.cafeteriabackend.application.port.in.order;

import com.gonzalo.cafeteriabackend.domain.model.Order;

public interface CreateOrder {
    CreateOrderResponse execute(CreateOrderRequest request);

    record CreateOrderRequest(Order order) {}

    record CreateOrderResponse(Order order) {}
}
