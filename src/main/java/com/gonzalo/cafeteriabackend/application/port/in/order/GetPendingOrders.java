package com.gonzalo.cafeteriabackend.application.port.in.order;

import com.gonzalo.cafeteriabackend.domain.model.Order;
import java.util.List;

public interface GetPendingOrders {
    GetPendingOrdersResponse execute(GetPendingOrdersRequest request);

    record GetPendingOrdersRequest() {}

    record GetPendingOrdersResponse(List<Order> orders) {}
}
