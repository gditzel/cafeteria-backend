package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.OrderRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.OrderRepository;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper.OrderEntityMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {
    private final OrderRepository orderRepository;

    public OrderRepositoryAdapter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findPendingOrdersWithItems() {
        return orderRepository.findPendingOrdersWithItems().stream()
                .map(OrderEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Order> findAllActiveByTable(Long tableId) {
        return orderRepository.findAllActiveByTable(tableId).stream()
                .map(OrderEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id).map(OrderEntityMapper::toDomain);
    }

    @Override
    public Order save(Order order) {
        return OrderEntityMapper.toDomain(
                orderRepository.save(OrderEntityMapper.toEntity(order)));
    }

    @Override
    public void saveAndFlush(Order order) {
        orderRepository.saveAndFlush(OrderEntityMapper.toEntity(order));
    }
}
