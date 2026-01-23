package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.Order;
import com.gonzalo.cafeteriabackend.service.OrderService;
import com.gonzalo.cafeteriabackend.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/active/{tableId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Order> getActiveOrder(@PathVariable Long tableId) {
        Order order = orderService.getConsumoAcumulado(tableId);
        return (order != null) ? ResponseEntity.ok(order) : ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Order>> getPendingOrders() {
        return ResponseEntity.ok(orderRepository.findPendingOrdersWithItems());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/close/{tableId}")
    public ResponseEntity<Void> closeOrder(@PathVariable Long tableId) {
        orderService.closeOrder(tableId);
        return ResponseEntity.ok().build();
    }
}