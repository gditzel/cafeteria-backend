package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.table.GetAllTables;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.TableDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.TableMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:4200")
public class TableController {
    private final GetAllTables getAllTables;

    public TableController(GetAllTables getAllTables) {
        this.getAllTables = getAllTables;
    }

    @GetMapping
    public List<TableDto> getAllTables() {
        var response = getAllTables.execute(new GetAllTables.GetAllTablesRequest());
        return response.tables().stream()
                .map(TableMapper::toDto)
                .toList();
    }
}
