package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.Order;
import com.gonzalo.cafeteriabackend.repository.OrderRepository;
import com.gonzalo.cafeteriabackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepo;

    public OrderController(OrderService orderService, OrderRepository orderRepo) {
        this.orderService = orderService;
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public Order create(@RequestBody Order orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    @GetMapping("/active")
    public List<Order> getActiveOrders() {
        return orderRepo.findByStatus(Order.Status.PENDIENTE);
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));

        order.setStatus(status);
        return orderRepo.save(order);
    }
}