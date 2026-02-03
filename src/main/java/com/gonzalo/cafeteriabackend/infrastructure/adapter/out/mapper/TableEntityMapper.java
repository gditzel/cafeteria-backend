package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.Table;

public class TableEntityMapper {
    private TableEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.TableEntity toEntity(
            Table domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.TableEntity entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.TableEntity();
        entity.setId(domain.getId());
        entity.setNumber(domain.getNumber());
        entity.setStatus(domain.getStatus());
        return entity;
    }

    public static Table toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.TableEntity entity) {
        if (entity == null) {
            return null;
        }
        Table domain = new Table();
        domain.setId(entity.getId());
        domain.setNumber(entity.getNumber());
        domain.setStatus(entity.getStatus());
        return domain;
    }
}
