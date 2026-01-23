package com.gonzalo.cafeteriabackend.application.port.in;

import com.gonzalo.cafeteriabackend.domain.model.Table;
import java.util.List;

public interface TableUseCase {
    List<Table> getAllTables();
}
