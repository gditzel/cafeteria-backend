package com.gonzalo.cafeteriabackend.application.port.in.order;

import com.gonzalo.cafeteriabackend.domain.model.Order;

public interface GetAccumulatedConsumption {
    GetAccumulatedConsumptionResponse execute(GetAccumulatedConsumptionRequest request);

    record GetAccumulatedConsumptionRequest(Long tableId) {}

    record GetAccumulatedConsumptionResponse(Order order) {}
}
