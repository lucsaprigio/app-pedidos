package com.aprigio.pedidos.infra.persistence;

import com.aprigio.pedidos.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> { }
