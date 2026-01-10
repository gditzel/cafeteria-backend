package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.*;
import com.gonzalo.cafeteriabackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (product.getStock() < item.getQuantity()) {
                // Buscamos el nombre en español para el mensaje de error
                String productName = product.getTranslations().stream()
                        .filter(t -> t.getLanguageCode().equals("es"))
                        .map(ProductTranslation::getName)
                        .findFirst().orElse("Producto");

                throw new RuntimeException("No hay stock suficiente de: " + productName);
            }

            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubtotal(subtotal);
            item.setProduct(product);
            item.setOrder(order);
            total = total.add(subtotal);
        }

        order.setTotal(total);
        order.setStatus(Order.Status.PENDIENTE);
        return orderRepository.save(order);
    }
}