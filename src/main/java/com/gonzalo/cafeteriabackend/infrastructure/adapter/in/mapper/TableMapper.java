package com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.TableDto;
import com.gonzalo.cafeteriabackend.domain.model.Table;

public class TableMapper {
    private TableMapper() {
    }

    public static TableDto toDto(Table table) {
        if (table == null) {
            return null;
        }
        TableDto dto = new TableDto();
        dto.setId(table.getId());
        dto.setNumber(table.getNumber());
        dto.setStatus(table.getStatus());
        return dto;
    }

    public static Table toEntity(TableDto dto) {
        if (dto == null) {
            return null;
        }
        Table table = new Table();
        table.setId(dto.getId());
        table.setNumber(dto.getNumber());
        table.setStatus(dto.getStatus());
        return table;
    }
}
