package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.OrderResponse;
import com.aprigio.pedidos.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindAllOrdersUseCase {

    private final OrderRepository repository;

    public List<OrderResponse> execute() {
        /*
         * Padrão "lista de entidades -> lista de DTOs": .stream().map().toList().
         *
         *   // TypeScript / NestJS:
         *   const orders = await this.repo.findAll();
         *   return orders.map(OrderResponse.from);
         *
         * Aqui não tem await porque findAll() é síncrono/bloqueante (JDBC). No Node o acesso
         * ao banco é assíncrono, então lá viria um await antes do .map().
         */
        return repository.findAll()
                .stream()
                .map(OrderResponse::from)
                .toList();
    }
}
