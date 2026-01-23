package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.*;
import com.gonzalo.cafeteriabackend.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final ProductRepository productRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderService(OrderRepository orderRepository, TableRepository tableRepository,
                        ProductRepository productRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.productRepository = productRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public Order getConsumoAcumulado(Long tableId) {
        List<Order> activeOrders = orderRepository.findAllActiveByTable(tableId);
        if (activeOrders.isEmpty()) return null;

        Order totalMesa = new Order();
        totalMesa.setTable(activeOrders.get(0).getTable());

        // Mapa para agrupar ítems por ID de producto
        Map<Long, OrderItem> groupedItems = new LinkedHashMap<>();
        BigDecimal acumuladoMesa = BigDecimal.ZERO;

        for (Order o : activeOrders) {
            for (OrderItem item : o.getItems()) {
                Long productId = item.getProduct().getId();
                if (groupedItems.containsKey(productId)) {
                    // Si el producto ya existe en el resumen, sumamos cantidad y subtotal
                    OrderItem existing = groupedItems.get(productId);
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    existing.setSubtotal(existing.getSubtotal().add(item.getSubtotal()));
                } else {
                    // Si es nuevo, creamos una copia para no alterar la persistencia
                    OrderItem newItem = new OrderItem();
                    newItem.setProduct(item.getProduct());
                    newItem.setQuantity(item.getQuantity());
                    newItem.setSubtotal(item.getSubtotal());
                    groupedItems.put(productId, newItem);
                }
            }
            if (o.getTotal() != null) acumuladoMesa = acumuladoMesa.add(o.getTotal());
        }

        totalMesa.setItems(new ArrayList<>(groupedItems.values()));
        totalMesa.setTotal(acumuladoMesa);
        return totalMesa;
    }

    @Transactional
    public Order createOrder(Order order) {
        TableEntity table = tableRepository.findById(order.getTable().getId()).orElseThrow();
        table.setStatus("OCCUPIED");
        tableRepository.save(table);

        BigDecimal totalPedido = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // VALIDACIÓN DE SEGURIDAD: Evita stock negativo
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " +
                        (product.getTranslations().isEmpty() ? "Producto ID " + product.getId() : product.getTranslations().get(0).getName()));
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            item.setOrder(order);
            BigDecimal sub = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setSubtotal(sub);
            totalPedido = totalPedido.add(sub);
        }

        order.setTotal(totalPedido);
        order.setStatus("PENDIENTE");
        Order savedOrder = orderRepository.save(order);
        messagingTemplate.convertAndSend("/topic/orders", "REFRESH");
        return savedOrder;
    }

    @Transactional
    public void closeOrder(Long tableId) {
        List<Order> orders = orderRepository.findAllActiveByTable(tableId);
        for (Order o : orders) {
            o.setStatus("CLOSED");
            orderRepository.save(o);
        }
        TableEntity table = tableRepository.findById(tableId).orElseThrow();
        table.setStatus("FREE");
        tableRepository.saveAndFlush(table);
        messagingTemplate.convertAndSend("/topic/orders", "REFRESH");
    }

    @Transactional
    public void updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.saveAndFlush(order);
        messagingTemplate.convertAndSend("/topic/orders", "REFRESH");
    }
}