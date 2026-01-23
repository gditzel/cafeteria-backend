package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.OrderUseCase;
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
    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @GetMapping("/active/{tableId}")
    @Transactional(readOnly = true)
    public ResponseEntity<OrderDto> getActiveOrder(@PathVariable Long tableId) {
        var order = orderUseCase.getConsumoAcumulado(tableId);
        return (order != null)
                ? ResponseEntity.ok(OrderMapper.toDto(order))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderDto>> getPendingOrders() {
        return ResponseEntity.ok(orderUseCase.getPendingOrders().stream()
                .map(OrderMapper::toDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto order) {
        return ResponseEntity.ok(OrderMapper.toDto(orderUseCase.createOrder(OrderMapper.toEntity(order))));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderUseCase.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/close/{tableId}")
    public ResponseEntity<Void> closeOrder(@PathVariable Long tableId) {
        orderUseCase.closeOrder(tableId);
        return ResponseEntity.ok().build();
    }
}
