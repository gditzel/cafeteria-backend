package com.gonzalo.cafeteriabackend.application.port.in.order;

public interface UpdateOrderStatus {
    UpdateOrderStatusResponse execute(UpdateOrderStatusRequest request);

    record UpdateOrderStatusRequest(Long orderId, String status) {}

    record UpdateOrderStatusResponse(boolean updated) {}
}
