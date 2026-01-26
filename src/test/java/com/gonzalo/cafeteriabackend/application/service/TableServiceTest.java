package com.gonzalo.cafeteriabackend.application.usecase.table;

import com.gonzalo.cafeteriabackend.application.port.in.table.GetAllTables;
import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableUseCasesTest {

    @Mock
    private TableRepositoryPort tableRepository;

    @InjectMocks
    private TableUseCases tableUseCases;

    @Test
    void getAllTablesUsesRepositoryOrder() {
        Table table = new Table();
        when(tableRepository.findAllByOrderByNumberAsc()).thenReturn(List.of(table));

        List<Table> result = tableUseCases
                .execute(new GetAllTables.GetAllTablesRequest())
                .tables();

        assertThat(result).containsExactly(table);
    }
}
