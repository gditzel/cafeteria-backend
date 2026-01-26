package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.order.CloseOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.CreateOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetAccumulatedConsumption;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetPendingOrders;
import com.gonzalo.cafeteriabackend.application.port.in.order.UpdateOrderStatus;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.OrderDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    private final GetAccumulatedConsumption getAccumulatedConsumption;
    private final GetPendingOrders getPendingOrders;
    private final CreateOrder createOrder;
    private final UpdateOrderStatus updateOrderStatus;
    private final CloseOrder closeOrder;

    public OrderController(
            GetAccumulatedConsumption getAccumulatedConsumption,
            GetPendingOrders getPendingOrders,
            CreateOrder createOrder,
            UpdateOrderStatus updateOrderStatus,
            CloseOrder closeOrder) {
        this.getAccumulatedConsumption = getAccumulatedConsumption;
        this.getPendingOrders = getPendingOrders;
        this.createOrder = createOrder;
        this.updateOrderStatus = updateOrderStatus;
        this.closeOrder = closeOrder;
    }

    @GetMapping("/active/{tableId}")
    @Transactional(readOnly = true)
    public ResponseEntity<OrderDto> getActiveOrder(@PathVariable Long tableId) {
        var response = getAccumulatedConsumption
                .execute(new GetAccumulatedConsumption.GetAccumulatedConsumptionRequest(tableId));
        var order = response.order();
        return (order != null)
                ? ResponseEntity.ok(OrderMapper.toDto(order))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderDto>> getPendingOrders() {
        var response = getPendingOrders.execute(new GetPendingOrders.GetPendingOrdersRequest());
        return ResponseEntity.ok(response.orders().stream()
                .map(OrderMapper::toDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto order) {
        var response = createOrder.execute(new CreateOrder.CreateOrderRequest(OrderMapper.toEntity(order)));
        return ResponseEntity.ok(OrderMapper.toDto(response.order()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        updateOrderStatus.execute(new UpdateOrderStatus.UpdateOrderStatusRequest(id, status));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/close/{tableId}")
    public ResponseEntity<Void> closeOrder(@PathVariable Long tableId) {
        closeOrder.execute(new CloseOrder.CloseOrderRequest(tableId));
        return ResponseEntity.ok().build();
    }
}
