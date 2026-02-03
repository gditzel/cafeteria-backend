package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.OrderItem;

public class OrderItemEntityMapper {
    private OrderItemEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem toEntity(
            OrderItem domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem();
        entity.setId(domain.getId());
        entity.setQuantity(domain.getQuantity());
        entity.setSubtotal(domain.getSubtotal());
        entity.setProduct(ProductEntityMapper.toEntity(domain.getProduct()));
        return entity;
    }

    public static OrderItem toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.OrderItem entity) {
        if (entity == null) {
            return null;
        }
        OrderItem domain = new OrderItem();
        domain.setId(entity.getId());
        domain.setQuantity(entity.getQuantity());
        domain.setSubtotal(entity.getSubtotal());
        domain.setProduct(ProductEntityMapper.toDomain(entity.getProduct()));
        return domain;
    }
}
