package com.aprigio.pedidos.domain.repository;

import com.aprigio.pedidos.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    boolean existsById(Long id);
}
