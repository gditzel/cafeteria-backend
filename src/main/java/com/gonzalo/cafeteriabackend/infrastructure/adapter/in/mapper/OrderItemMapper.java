package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.OrderItemDto;
import com.gonzalo.cafeteriabackend.domain.model.OrderItem;
import com.gonzalo.cafeteriabackend.domain.model.Product;

public class OrderItemMapper {
    private OrderItemMapper() {
    }

    public static OrderItemDto toDto(OrderItem item) {
        if (item == null) {
            return null;
        }
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        dto.setProduct(ProductMapper.toDto(item.getProduct()));
        return dto;
    }

    public static OrderItem toEntity(OrderItemDto dto) {
        if (dto == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        item.setId(dto.getId());
        item.setQuantity(dto.getQuantity());
        item.setSubtotal(dto.getSubtotal());
        item.setProduct(mapProduct(dto));
        return item;
    }

    private static Product mapProduct(OrderItemDto dto) {
        if (dto.getProduct() == null) {
            return null;
        }
        return ProductMapper.toEntity(dto.getProduct());
    }
}
