package com.gonzalo.cafeteriabackend.application.port.in.table;

import com.gonzalo.cafeteriabackend.domain.model.Table;
import java.util.List;

public interface GetAllTables {
    GetAllTablesResponse execute(GetAllTablesRequest request);

    record GetAllTablesRequest() {}

    record GetAllTablesResponse(List<Table> tables) {}
}
