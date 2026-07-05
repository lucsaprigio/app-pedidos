package com.aprigio.pedidos.infra.persistence;

import com.aprigio.pedidos.domain.entity.Order;
import com.aprigio.pedidos.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpa;

    @Override
    public Order save(Order order) {return jpa.save(order) ; }

    @Override
    public Optional<Order> findById(Long id) { return jpa.findById(id); }

    @Override
    public List<Order> findAll() {return jpa.findAll(); }

    @Override
    public boolean existsById(Long id) { return jpa.existsById(id); }

}
