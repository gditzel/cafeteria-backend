package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.TableRepository;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.mapper.TableEntityMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class TableRepositoryAdapter implements TableRepositoryPort {
    private final TableRepository tableRepository;

    public TableRepositoryAdapter(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public Optional<Table> findById(Long id) {
        return tableRepository.findById(id).map(TableEntityMapper::toDomain);
    }

    @Override
    public List<Table> findAllByOrderByNumberAsc() {
        return tableRepository.findAllByOrderByNumberAsc().stream()
                .map(TableEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Table save(Table table) {
        return TableEntityMapper.toDomain(
                tableRepository.save(TableEntityMapper.toEntity(table)));
    }

    @Override
    public void saveAndFlush(Table table) {
        tableRepository.saveAndFlush(TableEntityMapper.toEntity(table));
    }
}
