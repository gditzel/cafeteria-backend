package com.gonzalo.cafeteriabackend.application.usecase.table;

import com.gonzalo.cafeteriabackend.application.port.in.table.GetAllTables;
import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TableUseCases implements GetAllTables {
    private final TableRepositoryPort tableRepository;

    public TableUseCases(TableRepositoryPort tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public GetAllTablesResponse execute(GetAllTablesRequest request) {
        return new GetAllTablesResponse(tableRepository.findAllByOrderByNumberAsc());
    }
}
