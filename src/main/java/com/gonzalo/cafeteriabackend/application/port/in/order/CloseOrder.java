package com.gonzalo.cafeteriabackend.application.port.in.order;

public interface CloseOrder {
    CloseOrderResponse execute(CloseOrderRequest request);

    record CloseOrderRequest(Long tableId) {}

    record CloseOrderResponse(boolean closed) {}
}
