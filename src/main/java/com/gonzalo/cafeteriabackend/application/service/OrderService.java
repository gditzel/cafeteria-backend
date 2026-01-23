package com.gonzalo.cafeteriabackend.application.service;

import com.gonzalo.cafeteriabackend.application.port.in.OrderUseCase;
import com.gonzalo.cafeteriabackend.application.port.out.OrderNotificationPort;
import com.gonzalo.cafeteriabackend.application.port.out.OrderRepositoryPort;
import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.application.port.out.TableRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.Order;
import com.gonzalo.cafeteriabackend.domain.model.OrderItem;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import com.gonzalo.cafeteriabackend.domain.model.Table;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService implements OrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final TableRepositoryPort tableRepository;
    private final ProductRepositoryPort productRepository;
    private final OrderNotificationPort orderNotification;

    public OrderService(
            OrderRepositoryPort orderRepository,
            TableRepositoryPort tableRepository,
            ProductRepositoryPort productRepository,
            OrderNotificationPort orderNotification) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.productRepository = productRepository;
        this.orderNotification = orderNotification;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getConsumoAcumulado(Long tableId) {
        List<Order> activeOrders = orderRepository.findAllActiveByTable(tableId);
        if (activeOrders.isEmpty()) {
            return null;
        }

        Order totalMesa = new Order();
        totalMesa.setTable(activeOrders.get(0).getTable());

        Map<Long, OrderItem> groupedItems = new LinkedHashMap<>();
        BigDecimal acumuladoMesa = BigDecimal.ZERO;

        for (Order o : activeOrders) {
            for (OrderItem item : o.getItems()) {
                Long productId = item.getProduct().getId();
                if (groupedItems.containsKey(productId)) {
                    OrderItem existing = groupedItems.get(productId);
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    existing.setSubtotal(existing.getSubtotal().add(item.getSubtotal()));
                } else {
                    OrderItem newItem = new OrderItem();
                    newItem.setProduct(item.getProduct());
                    newItem.setQuantity(item.getQuantity());
                    newItem.setSubtotal(item.getSubtotal());
                    groupedItems.put(productId, newItem);
                }
            }
            if (o.getTotal() != null) {
                acumuladoMesa = acumuladoMesa.add(o.getTotal());
            }
        }

        totalMesa.setItems(new ArrayList<>(groupedItems.values()));
        totalMesa.setTotal(acumuladoMesa);
        return totalMesa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getPendingOrders() {
        return orderRepository.findPendingOrdersWithItems();
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        Table table = tableRepository.findById(order.getTable().getId()).orElseThrow();
        table.setStatus("OCCUPIED");
        tableRepository.save(table);

        BigDecimal totalPedido = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " +
                        (product.getTranslations().isEmpty()
                                ? "Producto ID " + product.getId()
                                : product.getTranslations().get(0).getName()));
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            BigDecimal sub = BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setSubtotal(sub);
            totalPedido = totalPedido.add(sub);
        }

        order.setTotal(totalPedido);
        order.setStatus("PENDIENTE");
        Order savedOrder = orderRepository.save(order);
        orderNotification.notifyOrdersRefresh();
        return savedOrder;
    }

    @Override
    @Transactional
    public void closeOrder(Long tableId) {
        List<Order> orders = orderRepository.findAllActiveByTable(tableId);
        for (Order o : orders) {
            o.setStatus("CLOSED");
            orderRepository.save(o);
        }
        Table table = tableRepository.findById(tableId).orElseThrow();
        table.setStatus("FREE");
        tableRepository.saveAndFlush(table);
        orderNotification.notifyOrdersRefresh();
    }

    @Override
    @Transactional
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.saveAndFlush(order);
        orderNotification.notifyOrdersRefresh();
    }
}
