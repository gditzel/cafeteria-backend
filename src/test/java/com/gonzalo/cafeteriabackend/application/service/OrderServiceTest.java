package com.gonzalo.cafeteriabackend.application.usecase.order;

import com.gonzalo.cafeteriabackend.application.port.in.order.CloseOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.CreateOrder;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetAccumulatedConsumption;
import com.gonzalo.cafeteriabackend.application.port.in.order.GetPendingOrders;
import com.gonzalo.cafeteriabackend.application.port.in.order.UpdateOrderStatus;
import com.gonzalo.cafeteriabackend.application.port.out.OrderNotificationPort;
import com.gonzalo.cafeteriabackend.application.port.out.OrderRepositoryPort;
import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.domain.model.OrderItem;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCasesTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private TableRepositoryPort tableRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private OrderNotificationPort orderNotification;

    @InjectMocks
    private OrderUseCases orderUseCases;

    @Test
    void getConsumoAcumuladoReturnsNullWhenNoActiveOrders() {
        when(orderRepository.findAllActiveByTable(1L)).thenReturn(List.of());

        Order result = orderUseCases
                .execute(new GetAccumulatedConsumption.GetAccumulatedConsumptionRequest(1L))
                .order();

        assertThat(result).isNull();
    }

    @Test
    void getConsumoAcumuladoAggregatesItemsAndTotals() {
        Product product1 = new Product();
        product1.setId(10L);
        Product product2 = new Product();
        product2.setId(20L);

        OrderItem item1 = new OrderItem();
        item1.setProduct(product1);
        item1.setQuantity(1);
        item1.setSubtotal(new BigDecimal("10.00"));

        OrderItem item2 = new OrderItem();
        item2.setProduct(product2);
        item2.setQuantity(2);
        item2.setSubtotal(new BigDecimal("20.00"));

        OrderItem item3 = new OrderItem();
        item3.setProduct(product1);
        item3.setQuantity(3);
        item3.setSubtotal(new BigDecimal("30.00"));

        Table table = new Table();
        table.setId(1L);

        Order order1 = new Order();
        order1.setTable(table);
        order1.setItems(new ArrayList<>(List.of(item1, item2)));
        order1.setTotal(new BigDecimal("30.00"));

        Order order2 = new Order();
        order2.setTable(table);
        order2.setItems(new ArrayList<>(List.of(item3)));
        order2.setTotal(new BigDecimal("30.00"));

        when(orderRepository.findAllActiveByTable(1L)).thenReturn(List.of(order1, order2));

        Order result = orderUseCases
                .execute(new GetAccumulatedConsumption.GetAccumulatedConsumptionRequest(1L))
                .order();

        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("60.00"));
        assertThat(result.getItems()).hasSize(2);

        OrderItem aggregated1 = result.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(10L))
                .findFirst()
                .orElseThrow();
        assertThat(aggregated1.getQuantity()).isEqualTo(4);
        assertThat(aggregated1.getSubtotal()).isEqualByComparingTo(new BigDecimal("40.00"));
    }

    @Test
    void getPendingOrdersDelegatesToRepository() {
        Order order = new Order();
        when(orderRepository.findPendingOrdersWithItems()).thenReturn(List.of(order));

        List<Order> result = orderUseCases
                .execute(new GetPendingOrders.GetPendingOrdersRequest())
                .orders();

        assertThat(result).containsExactly(order);
    }

    @Test
    void createOrderSetsTotalsUpdatesStockAndNotifies() {
        Table table = new Table();
        table.setId(1L);
        table.setStatus("FREE");
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));

        Product product1 = new Product();
        product1.setId(10L);
        product1.setPrice(5.0);
        product1.setStock(5);
        product1.setTranslations(new ArrayList<>());

        Product product2 = new Product();
        product2.setId(20L);
        product2.setPrice(7.5);
        product2.setStock(1);
        product2.setTranslations(new ArrayList<>());

        when(productRepository.findById(10L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(20L)).thenReturn(Optional.of(product2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderItem item1 = new OrderItem();
        Product itemProduct1 = new Product();
        itemProduct1.setId(10L);
        item1.setProduct(itemProduct1);
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        Product itemProduct2 = new Product();
        itemProduct2.setId(20L);
        item2.setProduct(itemProduct2);
        item2.setQuantity(1);

        Order order = new Order();
        order.setTable(table);
        order.setItems(new ArrayList<>(List.of(item1, item2)));

        Order saved = orderUseCases
                .execute(new CreateOrder.CreateOrderRequest(order))
                .order();

        assertThat(saved.getStatus()).isEqualTo("PENDIENTE");
        assertThat(saved.getTotal()).isEqualByComparingTo(new BigDecimal("17.50"));
        assertThat(item1.getSubtotal()).isEqualByComparingTo(new BigDecimal("10.00"));
        assertThat(item2.getSubtotal()).isEqualByComparingTo(new BigDecimal("7.50"));
        assertThat(product1.getStock()).isEqualTo(3);
        assertThat(product2.getStock()).isEqualTo(0);

        ArgumentCaptor<Table> tableCaptor = ArgumentCaptor.forClass(Table.class);
        verify(tableRepository).save(tableCaptor.capture());
        assertThat(tableCaptor.getValue().getStatus()).isEqualTo("OCCUPIED");

        verify(orderNotification).notifyOrdersRefresh();
    }

    @Test
    void createOrderThrowsWhenInsufficientStock() {
        Table table = new Table();
        table.setId(1L);
        when(tableRepository.findById(1L)).thenReturn(Optional.of(table));

        Product product = new Product();
        product.setId(10L);
        product.setPrice(5.0);
        product.setStock(1);
        product.setTranslations(new ArrayList<>());
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        OrderItem item = new OrderItem();
        Product itemProduct = new Product();
        itemProduct.setId(10L);
        item.setProduct(itemProduct);
        item.setQuantity(2);

        Order order = new Order();
        order.setTable(table);
        order.setItems(new ArrayList<>(List.of(item)));

        assertThatThrownBy(() -> orderUseCases.execute(new CreateOrder.CreateOrderRequest(order)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void closeOrderClosesOrdersAndTableAndNotifies() {
        Order order1 = new Order();
        order1.setStatus("PENDIENTE");
        Order order2 = new Order();
        order2.setStatus("PENDIENTE");
        when(orderRepository.findAllActiveByTable(5L)).thenReturn(List.of(order1, order2));

        Table table = new Table();
        table.setId(5L);
        table.setStatus("OCCUPIED");
        when(tableRepository.findById(5L)).thenReturn(Optional.of(table));

        orderUseCases.execute(new CloseOrder.CloseOrderRequest(5L));

        assertThat(order1.getStatus()).isEqualTo("CLOSED");
        assertThat(order2.getStatus()).isEqualTo("CLOSED");
        assertThat(table.getStatus()).isEqualTo("FREE");
        verify(orderRepository).save(order1);
        verify(orderRepository).save(order2);
        verify(tableRepository).saveAndFlush(table);
        verify(orderNotification).notifyOrdersRefresh();
    }

    @Test
    void updateStatusUpdatesOrderAndNotifies() {
        Order order = new Order();
        order.setStatus("PENDIENTE");
        when(orderRepository.findById(8L)).thenReturn(Optional.of(order));

        orderUseCases.execute(new UpdateOrderStatus.UpdateOrderStatusRequest(8L, "READY"));

        assertThat(order.getStatus()).isEqualTo("READY");
        verify(orderRepository).saveAndFlush(order);
        verify(orderNotification).notifyOrdersRefresh();
    }
}
