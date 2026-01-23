package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper;

import com.gonzalo.cafeteriabackend.domain.model.BinaryData;

public class BinaryDataEntityMapper {
    private BinaryDataEntityMapper() {
    }

    public static com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.BinaryData toEntity(
            BinaryData domain) {
        if (domain == null) {
            return null;
        }
        com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.BinaryData entity =
                new com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.BinaryData();
        entity.setId(domain.getId());
        entity.setContentType(domain.getContentType());
        entity.setData(domain.getData());
        return entity;
    }

    public static BinaryData toDomain(
            com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.BinaryData entity) {
        if (entity == null) {
            return null;
        }
        BinaryData domain = new BinaryData();
        domain.setId(entity.getId());
        domain.setContentType(entity.getContentType());
        domain.setData(entity.getData());
        return domain;
    }
}
