package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.Order;
import com.gonzalo.cafeteriabackend.model.OrderItem;
import com.gonzalo.cafeteriabackend.model.Product;
import com.gonzalo.cafeteriabackend.repository.OrderRepository;
import com.gonzalo.cafeteriabackend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("No hay stock suficiente de: " + product.getName());
            }

            // Descontar stock
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // Calcular subtotales
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setSubtotal(subtotal);
            item.setProduct(product);
            item.setOrder(order);

            total = total.add(subtotal);
        }

        order.setTotal(total);
        order.setStatus(Order.Status.PENDIENTE);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}