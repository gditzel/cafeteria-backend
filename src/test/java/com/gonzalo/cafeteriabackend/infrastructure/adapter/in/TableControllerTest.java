package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.table.GetAllTables;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableController.class)
class TableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAllTables getAllTables;

    @Test
    void getAllTablesReturnsList() throws Exception {
        Table table = new Table();
        table.setId(1L);
        table.setNumber(10);
        table.setStatus("FREE");

        when(getAllTables.execute(new GetAllTables.GetAllTablesRequest()))
                .thenReturn(new GetAllTables.GetAllTablesResponse(List.of(table)));

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].number").value(10))
                .andExpect(jsonPath("$[0].status").value("FREE"));
    }
}
