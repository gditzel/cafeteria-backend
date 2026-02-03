package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.domain.model.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {
    private OrderEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order toEntity(Order domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order();
        entity.setId(domain.getId());
        entity.setUser(UserEntityMapper.toEntity(domain.getUser()));
        entity.setTable(TableEntityMapper.toEntity(domain.getTable()));
        entity.setItems(mapItemsToEntity(domain.getItems(), entity));
        entity.setTotal(domain.getTotal());
        entity.setStatus(domain.getStatus());
        return entity;
    }

    public static Order toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order entity) {
        if (entity == null) {
            return null;
        }
        Order domain = new Order();
        domain.setId(entity.getId());
        domain.setUser(UserEntityMapper.toDomain(entity.getUser()));
        domain.setTable(TableEntityMapper.toDomain(entity.getTable()));
        domain.setItems(mapItemsToDomain(entity.getItems()));
        domain.setTotal(entity.getTotal());
        domain.setStatus(entity.getStatus());
        return domain;
    }

    private static List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem>
            mapItemsToEntity(
            List<OrderItem> items,
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.Order orderEntity) {
        if (items == null) {
            return null;
        }
        List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem> mapped =
                items.stream()
                .map(OrderItemEntityMapper::toEntity)
                .collect(Collectors.toList());
        mapped.forEach(i -> i.setOrder(orderEntity));
        return mapped;
    }

    private static List<OrderItem> mapItemsToDomain(
            List<com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(OrderItemEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}
