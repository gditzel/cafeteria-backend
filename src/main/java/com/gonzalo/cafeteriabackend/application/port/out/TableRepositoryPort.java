package com.gonzalo.cafeteriabackend.application.port.out;

import com.gonzalo.cafeteriabackend.domain.model.Table;
import java.util.Optional;

public interface TableRepositoryPort {
    java.util.List<Table> findAllByOrderByNumberAsc();

    Optional<Table> findById(Long id);

    Table save(Table table);

    void saveAndFlush(Table table);
}
