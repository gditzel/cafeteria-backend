package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonzalo.cafeteriabackend.application.port.in.order.CloseOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.CreateOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetAccumulatedConsumption;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetPendingOrders;
import com.gonzalo.cafeteriabackend.application.port.in.order.UpdateOrderStatus;
import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.OrderDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.TableDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetAccumulatedConsumption getAccumulatedConsumption;

    @MockBean
    private GetPendingOrders getPendingOrders;

    @MockBean
    private CreateOrder createOrder;

    @MockBean
    private UpdateOrderStatus updateOrderStatus;

    @MockBean
    private CloseOrder closeOrder;

    @Test
    void getActiveOrderReturnsNoContentWhenMissing() throws Exception {
        when(getAccumulatedConsumption.execute(new GetAccumulatedConsumption.GetAccumulatedConsumptionRequest(2L)))
                .thenReturn(new GetAccumulatedConsumption.GetAccumulatedConsumptionResponse(null));

        mockMvc.perform(get("/api/orders/active/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getActiveOrderReturnsOrder() throws Exception {
        Order order = new Order();
        order.setId(3L);
        order.setTotal(new BigDecimal("12.50"));
        Table table = new Table();
        table.setId(1L);
        order.setTable(table);

        when(getAccumulatedConsumption.execute(new GetAccumulatedConsumption.GetAccumulatedConsumptionRequest(1L)))
                .thenReturn(new GetAccumulatedConsumption.GetAccumulatedConsumptionResponse(order));

        mockMvc.perform(get("/api/orders/active/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.total").value(12.50));
    }

    @Test
    void getPendingOrdersReturnsList() throws Exception {
        Order order = new Order();
        order.setId(4L);
        when(getPendingOrders.execute(new GetPendingOrders.GetPendingOrdersRequest()))
                .thenReturn(new GetPendingOrders.GetPendingOrdersResponse(List.of(order)));

        mockMvc.perform(get("/api/orders/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4));
    }

    @Test
    void createOrderReturnsSavedOrder() throws Exception {
        Order saved = new Order();
        saved.setId(7L);
        when(createOrder.execute(any(CreateOrder.CreateOrderRequest.class)))
                .thenReturn(new CreateOrder.CreateOrderResponse(saved));

        OrderDto dto = new OrderDto();
        TableDto tableDto = new TableDto();
        tableDto.setId(1L);
        dto.setTable(tableDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void updateStatusCallsUseCase() throws Exception {
        mockMvc.perform(patch("/api/orders/9/status")
                        .param("status", "READY"))
                .andExpect(status().isOk());

        verify(updateOrderStatus).execute(new UpdateOrderStatus.UpdateOrderStatusRequest(9L, "READY"));
    }

    @Test
    void closeOrderCallsUseCase() throws Exception {
        mockMvc.perform(post("/api/orders/close/6"))
                .andExpect(status().isOk());

        verify(closeOrder).execute(new CloseOrder.CloseOrderRequest(6L));
    }
}
