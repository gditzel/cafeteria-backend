package com.gonzalo.cafeteriabackend.application.service;

import com.gonzalo.cafeteriabackend.application.port.in.TableUseCase;
import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TableService implements TableUseCase {
    private final TableRepositoryPort tableRepository;

    public TableService(TableRepositoryPort tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public List<Table> getAllTables() {
        return tableRepository.findAllByOrderByNumberAsc();
    }
}
