package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.Status status);
}
