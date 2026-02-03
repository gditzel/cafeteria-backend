package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.OrderDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.OrderItemDto;
import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.domain.model.OrderItem;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    private OrderMapper() {
    }

    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUser(UserMapper.toDto(order.getUser()));
        dto.setTable(TableMapper.toDto(order.getTable()));
        dto.setItems(mapItemsToDto(order.getItems()));
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        return dto;
    }

    public static Order toEntity(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(dto.getId());
        order.setUser(UserMapper.toEntity(dto.getUser()));
        order.setTable(TableMapper.toEntity(dto.getTable()));
        order.setItems(mapItemsToEntity(dto.getItems()));
        order.setTotal(dto.getTotal());
        order.setStatus(dto.getStatus());
        return order;
    }

    private static List<OrderItemDto> mapItemsToDto(List<OrderItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private static List<OrderItem> mapItemsToEntity(List<OrderItemDto> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(OrderItemMapper::toEntity)
                .collect(Collectors.toList());
    }
}
